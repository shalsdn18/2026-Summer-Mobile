package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 매수 가능 금액 응답 모델
data class TossBuyingPowerResponse(
    @SerializedName("result") val result: BuyingPowerResult
)
data class BuyingPowerResult(
    @SerializedName("currency") val currency: String, // "KRW", "USD"
    @SerializedName("cashBuyingPower") val cashBuyingPower: String // 현금 기반 매수 가능 총액
)

// 2. 판매 가능 수량 응답 모델
data class TossSellableQuantityResponse(
    @SerializedName("result") val result: SellableQuantityResult
)
data class SellableQuantityResult(
    @SerializedName("sellableQuantity") val sellableQuantity: String // 매도 가용 주식수
)

// 3. 매매 수수료율 리스트 응답 모델
data class TossCommissionsResponse(
    @SerializedName("result") val result: List<CommissionItem>
)
data class CommissionItem(
    @SerializedName("marketCountry") val marketCountry: String, // "KR" 또는 "US"
    @SerializedName("commissionRate") val commissionRate: String, // 수수료율 소수점 기호 문자열
    @SerializedName("startDate") val startDate: String?, // 유효 기점 시작일 (nullable)
    @SerializedName("endDate") val endDate: String? // 유효 기점 종료일 (nullable)
)
