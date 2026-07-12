package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 호가 데이터 모델
data class TossOrderbookResponse(
    @SerializedName("result") val result: OrderbookResult
)
data class OrderbookResult(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("asks") val asks: List<OrderbookEntry>,
    @SerializedName("bids") val bids: List<OrderbookEntry>
)
data class OrderbookEntry(
    @SerializedName("price") val price: String,
    @SerializedName("volume") val volume: String
)

// 2. 현재가 다건 조회 모델
data class TossPricesResponse(
    @SerializedName("result") val result: List<PriceItem>
)
data class PriceItem(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("lastPrice") val lastPrice: String,
    @SerializedName("currency") val currency: String
)

// 3. 최근 체결 내역 모델
data class TossTradesResponse(
    @SerializedName("result") val result: List<TradeItem>
)
data class TradeItem(
    @SerializedName("price") val price: String,
    @SerializedName("volume") val volume: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("currency") val currency: String
)

// 4. 상/하한가 모델
data class TossPriceLimitsResponse(
    @SerializedName("result") val result: PriceLimitsResult
)
data class PriceLimitsResult(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("upperLimitPrice") val upperLimitPrice: String,
    @SerializedName("lowerLimitPrice") val lowerLimitPrice: String,
    @SerializedName("currency") val currency: String
)

// 5. 캔들 차트 모델
data class TossCandlesResponse(
    @SerializedName("result") val result: CandlesResult
)
data class CandlesResult(
    @SerializedName("candles") val candles: List<CandleItem>,
    @SerializedName("nextBefore") val nextBefore: String
)
data class CandleItem(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("openPrice") val openPrice: String,
    @SerializedName("highPrice") val highPrice: String,
    @SerializedName("lowPrice") val lowPrice: String,
    @SerializedName("closePrice") val closePrice: String,
    @SerializedName("volume") val volume: String,
    @SerializedName("currency") val currency: String
)
