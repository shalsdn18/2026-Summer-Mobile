package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 조건주문 목록 조회 전체 응답 모델
data class TossConditionalOrderListResponse(
    @SerializedName("result") val result: ConditionalOrderListResult
)

data class ConditionalOrderListResult(
    @SerializedName("conditionalOrders") val conditionalOrders: List<ConditionalOrderHistoryItem>,
    @SerializedName("nextCursor") val nextCursor: String?, // 다음 페이지 추적용 커서 토큰
    @SerializedName("hasNext") val hasNext: Boolean // 추가 페이지 존재 플래그
)

// 2. 단건 상세 조회 전체 응답 모델
data class TossConditionalOrderDetailResponse(
    @SerializedName("result") val result: ConditionalOrderHistoryItem
)

// 3. 개별 조건주문 마스터 내역 모델
data class ConditionalOrderHistoryItem(
    @SerializedName("conditionalOrderId") val conditionalOrderId: String, // 서버 발급 Opaque 고유 키
    @SerializedName("type") val type: String, // "SINGLE", "OCO", "OTO"
    @SerializedName("status") val status: String, // WATCHING, PAUSED, COMPLETED 등
    @SerializedName("symbol") val symbol: String,
    @SerializedName("market") val market: String, // "KR" 또는 "US"
    @SerializedName("quantity") val quantity: String,
    @SerializedName("orderType") val orderType: String, // "LIMIT", "MARKET"
    @SerializedName("expireDate") val expireDate: String, // 만료 기점 (YYYY-MM-DD)
    @SerializedName("first") val first: ConditionalLegDetail, // 선행 감시 레그 정보
    @SerializedName("second") val second: ConditionalLegDetail?, // 후행/교차 감시 레그 정보 (nullable)
    @SerializedName("createdAt") val createdAt: String // ISO 8601 KST 타임스탬프
)

// 4. 세부 감시 레그(Leg) 객체 모델
data class ConditionalLegDetail(
    @SerializedName("type") val type: String, // "STOP" 등
    @SerializedName("status") val status: String, // "WATCHING" 등
    @SerializedName("triggerPrice") val triggerPrice: String, // 감시 목표가
    @SerializedName("targetProfitRate") val targetProfitRate: String?,
    @SerializedName("orderPrice") val orderPrice: String?, // 호가 유형이 LIMIT 일 때 활성화
    @SerializedName("triggeredOrderId") val triggeredOrderId: String? // 조건 충족 후 발동된 실제 주문의 고유 ID
)
