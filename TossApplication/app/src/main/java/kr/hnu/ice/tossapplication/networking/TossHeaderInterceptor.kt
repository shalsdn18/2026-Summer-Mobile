package kr.hnu.ice.tossapplication.networking

import okhttp3.Interceptor
import okhttp3.Response

class TossHeaderInterceptor : Interceptor {
    
    // [Pull Model] 직접 변수를 들고 있지 않고 Manager/Session 클래스에서 실시간 캐시를 읽어옴
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        val urlPath = originalRequest.url.encodedPath

        // 1. [Constraint] Auth 그룹 엔드포인트(/oauth2/token)를 제외하고 실시간 메모리 캐시 토큰 주입
        if (!urlPath.contains("oauth2/token")) {
            TossTokenManager.getCachedToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        // 2. 사설 자산 API 전용 계좌 식별자 인젝션 (Requires-Account 헤더 감지 시)
        val requiresAccount = originalRequest.header("Requires-Account")
        if (requiresAccount != null && requiresAccount.toBoolean()) {
            requestBuilder.removeHeader("Requires-Account")
            TossAccountSession.getActiveAccountSeq()?.let { accountSeq ->
                requestBuilder.addHeader("X-Tossinvest-Account", accountSeq)
            } ?: throw IllegalStateException("계좌 식별 헤더가 누락되었습니다.")
        }

        return chain.proceed(requestBuilder.build())
    }
}
