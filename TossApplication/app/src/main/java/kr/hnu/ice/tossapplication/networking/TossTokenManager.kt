package kr.hnu.ice.tossapplication.networking

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

import kr.hnu.ice.tossapplication.utils.TossEncryptedStorage
import android.content.Context

object TossTokenManager {
    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var tokenExpiredTimeMillis: Long = 0L
    private var storage: TossEncryptedStorage? = null

    private const val CLIENT_ID = "tsck_live_uRm9Jp0kFJWheKVTL6JcEF" // 예시 클라이언트 ID
    private const val CLIENT_SECRET = "tssk_live_Yy1ET8xyQYk9Jl56TzQQ0vWuW3JJb1GrzrHmaHGYJ1G" // 예시 시크릿

    fun init(context: Context) {
        storage = TossEncryptedStorage.getInstance(context)
        cachedToken = storage?.getToken()
    }

    /**
     * 인터셉터 전용 동기식 토큰 Getter
     */
    fun getCachedToken(): String? = cachedToken

    /**
     * 유효한 액세스 토큰을 스레드 안정성을 보장하며 획득합니다.
     */
    suspend fun getValidToken(): String {
        return mutex.withLock {
            val currentTime = System.currentTimeMillis()
            
            // 1. 메모리 캐시 또는 저장소 토큰 확인 (만료 시간 관리 로직은 서버 응답 기반으로 단순화)
            if (cachedToken != null && currentTime < tokenExpiredTimeMillis - 300000L) {
                return@withLock cachedToken!!
            }

            // 2. 신규 발급
            try {
                val response = TossNetworkClient.apiService.getAccessToken(
                    grantType = "client_credentials",
                    clientId = CLIENT_ID,
                    clientSecret = CLIENT_SECRET
                )

                cachedToken = response.accessToken
                tokenExpiredTimeMillis = System.currentTimeMillis() + (response.expiresIn * 1000L)
                
                // 암호화 저장소에 즉시 커밋
                storage?.saveToken(response.accessToken)
                
                return@withLock response.accessToken
            } catch (e: Exception) {
                throw IllegalStateException("토스증권 OAuth 2.0 액세스 토큰 갱신 과정에서 예외가 발생했습니다.", e)
            }
        }
    }

    /**
     * 401 토큰 만료 에러 감지 시 캐시 및 저장소 데이터 명시적 파괴
     */
    fun invalidateToken() {
        cachedToken = null
        tokenExpiredTimeMillis = 0L
        storage?.clear()
    }
}
