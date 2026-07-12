package kr.hnu.ice.tossapplication.networking

import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

object TossErrorHandler {
    private val gson = Gson()

    /**
     * Retrofit 예외 객체를 파싱하여 공통 에러 구조체(TossErrorEnvelope)로 변환합니다.
     */
    fun parseError(throwable: Throwable): TossErrorEnvelope? {
        if (throwable is HttpException) {
            val response = throwable.response()
            val errorBodyString = response?.errorBody()?.string()
            
            if (!errorBodyString.isNullOrEmpty()) {
                return try {
                    gson.fromJson(errorBodyString, TossErrorEnvelope::class.java)
                } catch (e: Exception) {
                    // JSON 스펙 불일치 시 폴백 생성
                    createFallbackError("parse-failed", "에러 바디 파싱 연산 실패")
                }
            }
        } else if (throwable is IOException) {
            return createFallbackError("network-timeout", "네트워크 통신이 원활하지 않거나 타임아웃이 발생했습니다.")
        }
        return createFallbackError("unknown-error", throwable.localizedMessage ?: "알 수 없는 시스템 장애")
    }

    private fun createFallbackError(code: String, message: String): TossErrorEnvelope {
        return TossErrorEnvelope(
            error = ErrorDetails(
                requestId = "FALLBACK_ID_" + System.currentTimeMillis(),
                code = code,
                message = message,
                data = null
            )
        )
    }
}
