package kr.hnu.ice.tossapplication.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.hnu.ice.tossapplication.networking.OrderHistoryItem
import kr.hnu.ice.tossapplication.networking.TossAccountSession
import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.repository.TossOrderHistoryRepository

sealed class OrderHistoryUiState {
    object Loading : OrderHistoryUiState()
    data class Success(
        val orders: List<OrderHistoryItem>,
        val hasNext: Boolean,
        val isAppend: Boolean = false
    ) : OrderHistoryUiState()
    data class Error(val message: String) : OrderHistoryUiState()
}

class OrderHistoryViewModel : BaseViewModel() {
    private val _uiState = MutableStateFlow<OrderHistoryUiState>(OrderHistoryUiState.Loading)
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    private val accumulatedOrders = mutableListOf<OrderHistoryItem>()
    private var nextCursor: String? = null
    private var currentStatus: String = "OPEN"

    override fun handleException(throwable: Throwable, message: String) {
        _uiState.value = OrderHistoryUiState.Error(message)
    }

    /**
     * [Asymmetric Pagination] 탭 전환 또는 무한 스크롤(Scroll Load) 요청 시 호출됨
     */
    fun loadOrderHistory(status: String, isLoadMore: Boolean = false) {
        if (currentStatus != status) {
            currentStatus = status
            nextCursor = null
            accumulatedOrders.clear()
        }

        if (isLoadMore && nextCursor == null) return // 다음 페이지가 없으면 연산 단절

        viewModelScope.launch(exceptionHandler) {
            if (!isLoadMore) _uiState.value = OrderHistoryUiState.Loading

            // 계좌 식별 헤더 바인딩 보장
            TossAccountSession.refreshAndBindAccount(TossNetworkClient.apiService)

            // 파트 12에서 설계한 비대칭 분기 레포지토리 가동
            val response = TossOrderHistoryRepository.fetchOrderHistory(
                status = currentStatus,
                nextPageCursor = if (isLoadMore) nextCursor else null
            )

            val newOrders = response.result.orders
            nextCursor = response.result.nextCursor
            
            if (isLoadMore) {
                accumulatedOrders.addAll(newOrders)
            } else {
                accumulatedOrders.clear()
                accumulatedOrders.addAll(newOrders)
            }

            _uiState.value = OrderHistoryUiState.Success(
                orders = accumulatedOrders.toList(),
                hasNext = response.result.hasNext,
                isAppend = isLoadMore
            )
        }
    }
}
