package kr.hnu.ice.tossapplication.repository

import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.networking.TossOrderListResponse
import kr.hnu.ice.tossapplication.networking.OrderHistoryItem

object TossOrderHistoryRepository {
    private val apiService = TossNetworkClient.apiService

    /**
     * 상태 조건 유형에 맞춰 페이징 파라미터를 유동적으로 변형 튜닝하여 내역을 안전하게 수신합니다.
     */
    suspend fun fetchOrderHistory(
        status: String, // "OPEN" 또는 "CLOSED"
        symbol: String? = null,
        from: String? = null,
        to: String? = null,
        nextPageCursor: String? = null
    ): TossOrderListResponse {
        
        return if (status == "OPEN") {
            // [Constraint Guard] 진행 중 주문(OPEN) 아키텍처는 전량 무조건 반환이므로 페이징 제약 인자를 Null 처리하여 트래픽 최적화
            apiService.getOrderHistoryList(
                status = "OPEN",
                symbol = symbol,
                from = from,
                to = to,
                cursor = null,
                limit = null
            )
        } else {
            // 종료된 주문(CLOSED) 아키텍처는 안정적인 무한 스크롤 처리를 위해 limit 20 고정 배칭 및 커서 결합 가동
            apiService.getOrderHistoryList(
                status = "CLOSED",
                symbol = symbol,
                from = from,
                to = to,
                cursor = nextPageCursor,
                limit = 20 // 기본 페이징 버퍼 20건 구성
            )
        }
    }

    /**
     * 단일 주문 식별키를 기반으로 상세 체결 로그 정보를 메모리에 안전하게 반환합니다.
     */
    suspend fun fetchOrderDetail(orderId: String): OrderHistoryItem? {
        if (orderId.isBlank()) return null
        return try {
            val response = apiService.getOrderDetail(orderId)
            response.result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
