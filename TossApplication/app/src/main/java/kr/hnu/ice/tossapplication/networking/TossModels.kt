package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// Step 1: Core Models
data class TokenResponse(val accessToken: String, val tokenType: String, val expiresIn: Int)
data class StockPriceResponse(val symbol: String, val price: Double)
data class CandleResponse(val symbol: String, val candles: List<Candle>)
data class Candle(val time: String, val open: Double, val high: Double, val low: Double, val close: Double)
data class AccountResponse(val accountId: String, val accountName: String, val balance: Double)
data class HoldingsResponse(val holdings: List<Holding>)
data class Holding(val symbol: String, val quantity: Int, val averagePrice: Double)
data class OrderRequest(val symbol: String, val side: String, val quantity: Int, val price: Double)
data class OrderResponse(val orderId: String, val status: String)
data class BuyingPowerResponse(val currency: String, val amount: Double)
data class ConditionalRequest(val symbol: String, val targetPrice: Double, val side: String, val quantity: Int)
data class ConditionalOrderResponse(val conditionalOrderId: String)

// Part 3-15 Models (Legacy/Extended)
data class TossTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String, // "Bearer" 고정
    @SerializedName("expires_in") val expiresIn: Long    // 초 단위 유효 기간 (예: 86400)
)
