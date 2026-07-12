package kr.hnu.ice.tossapplication.networking

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.math.pow
import kotlin.random.Random

class TossRateLimitInterceptor : Interceptor {
    private val maxRetries = 3
    private val baseDelayMs = 1000L

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var retryCount = 0

        // HTTP 429 (Too Many Requests) 수신 시 루프 가동
        while (response.code == 429 && retryCount < maxRetries) {
            retryCount++
            response.close() // 기존 응답 리소스 해제 필수 (메모리 누수 방지)

            val retryAfterHeader = response.header("Retry-After")
            val delayMs = if (retryAfterHeader != null) {
                retryAfterHeader.toLongByOrNull() * 1000L
            } else {
                // 지수 백오프 계산: baseDelay * 2^retryCount
                val exponentialDelay = baseDelayMs * 2.0.pow(retryCount).toLong()
                // Jitter 추가 (계산된 지연 시간의 0% ~ 50% 사이의 무작위 값 추가)
                val jitter = Random.nextLong(0, exponentialDelay / 2)
                exponentialDelay + jitter
            }

            Log.w("TossNet", "Rate Limit 초과 (429). ${delayMs}ms 후 재시도 수행 ($retryCount/$maxRetries)")
            
            try {
                Thread.sleep(delayMs)
            } catch (e: InterruptedException) {
                throw IOException("재시도 대기 스레드 연산이 중단되었습니다.", e)
            }

            response = chain.proceed(request)
        }

        return response
    }

    private fun String.toLongByOrNull(): Long {
        return try { this.toLong() } catch (e: NumberFormatException) { 1L }
    }
}
