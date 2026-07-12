package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 단일/복합 감시 조건 서브 레그 모델
data class TossConditionRequest(
    @SerializedName("orderSide") val orderSide: String, // "BUY", "SELL"
    @SerializedName("triggerPrice") val triggerPrice: String, // 감시 목표가
    @SerializedName("orderPrice") val orderPrice: String? // LIMIT 일 때 필수, MARKET 일 때 전달 불가
)

// 2. 조건주문 생성 요청 전체 본문 모델
data class TossConditionalOrderCreateRequest(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("type") val type: String, // "SINGLE", "OCO", "OTO"
    @SerializedName("quantity") val quantity: String, // 그룹 공통 수량
    @SerializedName("orderType") val orderType: String, // "LIMIT", "MARKET"
    @SerializedName("expireDate") val expireDate: String, // 만료 기한 (YYYY-MM-DD)
    @SerializedName("first") val first: TossConditionRequest, // 필수 조건 레그
    @SerializedName("second") val second: TossConditionRequest? = null, // OCO/OTO 일 때 필수
    @SerializedName("clientOrderId") val clientOrderId: String? = null,
    @SerializedName("confirmHighValueOrder") val confirmHighValueOrder: Boolean = false
)

// 3. 조건주문 수정 요청 본문 모델 (수정 시 본문 내 symbol 필드는 전면 제외 규칙 준수)
data class TossConditionalOrderModifyRequest(
    @SerializedName("type") val type: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("orderType") val orderType: String,
    @SerializedName("expireDate") val expireDate: String,
    @SerializedName("first") val first: TossConditionRequest,
    @SerializedName("second") val second: TossConditionRequest? = null,
    @SerializedName("confirmHighValueOrder") val confirmHighValueOrder: Boolean = false
)

// 4. 생성 및 수정 처리 공통 응답 성공 모델
data class TossConditionalOrderResponse(
    @SerializedName("result") val result: ConditionalOrderResult
)

data class ConditionalOrderResult(
    @SerializedName("conditionalOrderId") val conditionalOrderId: String, // 새 발급 opaque 토큰 ID
    @SerializedName("clientOrderId") val clientOrderId: String?
)
