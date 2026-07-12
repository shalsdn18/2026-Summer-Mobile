package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 종목 기본 정보 다건 응답 모델
data class TossStockInfoResponse(
    @SerializedName("result") val result: List<StockInfoItem>
)

data class StockInfoItem(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("englishName") val englishName: String,
    @SerializedName("isinCode") val isinCode: String,
    @SerializedName("market") val market: String, // KOSPI, NASDAQ 등
    @SerializedName("securityType") val securityType: String,
    @SerializedName("isCommonShare") val isCommonShare: Boolean,
    @SerializedName("status") val status: String, // ACTIVE 등
    @SerializedName("currency") val currency: String,
    @SerializedName("listDate") val listDate: String,
    @SerializedName("delistDate") val delistDate: String?,
    @SerializedName("sharesOutstanding") val sharesOutstanding: String,
    @SerializedName("leverageFactor") val leverageFactor: Int?,
    @SerializedName("koreanMarketDetail") val koreanMarketDetail: KoreanMarketDetail?
)

data class KoreanMarketDetail(
    @SerializedName("liquidationTrading") val liquidationTrading: Boolean,
    @SerializedName("nxtSupported") val nxtSupported: Boolean,
    @SerializedName("krxTradingSuspended") val krxTradingSuspended: Boolean,
    @SerializedName("nxtTradingSuspended") val nxtTradingSuspended: Boolean
)

// 2. 매수 유의사항 및 VI 발동 응답 모델
data class TossStockWarningsResponse(
    @SerializedName("result") val result: List<StockWarningItem>
)

data class StockWarningItem(
    @SerializedName("warningType") val warningType: String, // OVERHEATED, VI_STATIC 등
    @SerializedName("exchange") val exchange: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String? // null 인 경우 현재 진행 중 상태
)
