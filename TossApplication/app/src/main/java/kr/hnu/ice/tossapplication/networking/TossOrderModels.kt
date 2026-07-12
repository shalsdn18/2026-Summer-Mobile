package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 주문 생성 요청 모델 (수량 기반 표준 필드)
data class TossOrderCreateRequest(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("side") val side: String, // "BUY", "SELL"
    @SerializedName("orderType") val orderType: String, // "LIMIT", "MARKET"
    @SerializedName("quantity") val quantity: String, // 소수점은 미주 시장가 매도만 허용
    @SerializedName("price") val price: String?, // LIMIT 일 때 필수
    @SerializedName("clientOrderId") val clientOrderId: String? = null, // 멱등성 토큰 (최대 36자)
    @SerializedName("confirmHighValueOrder") val confirmHighValueOrder: Boolean = false, // 1억원 이상 방어 플래그
    @SerializedName("timeInForce") val timeInForce: String = "DAY" // "DAY", "CLS"
)

// 2. 주문 정정 요청 모델
data class TossOrderModifyRequest(
    @SerializedName("orderType") val orderType: String, // "LIMIT", "MARKET"
    @SerializedName("price") val price: String?, // LIMIT 일 때 필수
    @SerializedName("quantity") val quantity: String?, // KR 은 필수, US 는 전달 불가(Null 고정)
    @SerializedName("confirmHighValueOrder") val confirmHighValueOrder: Boolean = false // 1억원 이상 방어 플래그
)

// 3. 주문 처리 공통 응답 모델
data class TossOrderResponse(
    @SerializedName("result") val result: OrderIdResult
)
data class OrderIdResult(
    @SerializedName("orderId") val orderId: String, // 서버 발급 Opaque 고유 토큰
    @SerializedName("clientOrderId") val clientOrderId: String?
)
