package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 주문 목록 조회 결과 컨테이너 모델
data class TossOrderListResponse(
    @SerializedName("result") val result: OrderListResult
)

data class OrderListResult(
    @SerializedName("orders") val orders: List<OrderHistoryItem>,
    @SerializedName("nextCursor") val nextCursor: String?, // 다음 페이지 포인터
    @SerializedName("hasNext") val hasNext: Boolean // 추가 데이터 존재 여부
)

// 2. 개별 주문 상세 조회 결과 모델
data class TossOrderDetailResponse(
    @SerializedName("result") val result: OrderHistoryItem
)

data class OrderHistoryItem(
    @SerializedName("orderId") val orderId: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("side") val side: String, // "BUY", "SELL"
    @SerializedName("orderType") val orderType: String, // "LIMIT", "MARKET"
    @SerializedName("timeInForce") val timeInForce: String, // "DAY", "CLS"
    @SerializedName("status") val status: String, // PENDING, FILLED, PARTIAL_FILLED 등
    @SerializedName("price") val price: String?,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("orderAmount") val orderAmount: String?,
    @SerializedName("currency") val currency: String, // "KRW", "USD"
    @SerializedName("orderedAt") val orderedAt: String, // ISO 8601 KST 타임스탬프
    @SerializedName("canceledAt") val canceledAt: String?,
    @SerializedName("execution") val execution: ExecutionDetails // 체결 핵심 내역 서브 노드
)

data class ExecutionDetails(
    @SerializedName("filledQuantity") val filledQuantity: String, // 체결 수량
    @SerializedName("averageFilledPrice") val averageFilledPrice: String?, // 평균 체결가
    @SerializedName("filledAmount") val filledAmount: String?, // 총 체결 금액
    @SerializedName("commission") val commission: String?, // 매매 수수료
    @SerializedName("tax") val tax: String?, // 거래세
    @SerializedName("filledAt") val filledAt: String?, // 최종 체결 시각
    @SerializedName("settlementDate") val settlementDate: String? // 결제 기준일 (YYYY-MM-DD)
)
