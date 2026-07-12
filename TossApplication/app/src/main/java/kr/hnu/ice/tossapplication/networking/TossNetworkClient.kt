package kr.hnu.ice.tossapplication.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object TossNetworkClient {
    private const val BASE_URL = "https://openapi.tossinvest.com/" // 토스증권 Open API 실서버 도메인
    
    val headerInterceptor = TossHeaderInterceptor()
    private val rateLimitInterceptor = TossRateLimitInterceptor()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(headerInterceptor)
        .addInterceptor(rateLimitInterceptor)
        .build()

    val apiService: TossSecuritiesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TossSecuritiesApiService::class.java)
    }
}
