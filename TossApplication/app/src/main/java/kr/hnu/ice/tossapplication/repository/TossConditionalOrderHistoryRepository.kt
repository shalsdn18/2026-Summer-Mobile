package kr.hnu.ice.tossapplication.repository

import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.networking.TossConditionalOrderListResponse
import kr.hnu.ice.tossapplication.networking.ConditionalOrderHistoryItem

object TossConditionalOrderHistoryRepository {
    private val apiService = TossNetworkClient.apiService

    /**
     * 커서 토큰 동기화 규칙을 준수하여 조건주문 리스트를 안전하게 페이징 수신합니다.
     */
    suspend fun fetchConditionalOrderHistory(
        status: String, // "OPEN" 또는 "CLOSED"
        symbol: String? = null,
        nextPageCursor: String? = null,
        pageSize: Int = 20
    ): TossConditionalOrderListResponse {
        
        // 상한 임계치 100건 강제 가드
        val validatedLimit = if (pageSize > 100) 100 else pageSize

        return apiService.getConditionalOrderHistoryList(
            status = status,
            symbol = symbol,
            cursor = nextPageCursor, // 커서 유무에 따른 동적 페이지 매핑 가동
            limit = validatedLimit
        )
    }

    /**
     * 단일 조건주문 식별 번호를 기점으로 실시간 감시 스택 세부를 안전하게 메모리로 복원합니다.
     */
    suspend fun fetchConditionalOrderDetail(conditionalOrderId: String): ConditionalOrderHistoryItem? {
        if (conditionalOrderId.isBlank()) return null
        return try {
            val response = apiService.getConditionalOrderDetail(conditionalOrderId)
            response.result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
