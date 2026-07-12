package kr.hnu.ice.tossapplication.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.networking.TossConditionalOrderCreateRequest
import kr.hnu.ice.tossapplication.networking.TossConditionalOrderModifyRequest
import kr.hnu.ice.tossapplication.networking.ConditionalOrderHistoryItem
import kr.hnu.ice.tossapplication.networking.TossAccountSession
import kr.hnu.ice.tossapplication.repository.TossConditionalOrderHistoryRepository
import kr.hnu.ice.tossapplication.utils.TossConditionalOrderGuard

sealed class ConditionalOrderUiState {
    object Loading : ConditionalOrderUiState()
    data class ListSuccess(val schedulers: List<ConditionalOrderHistoryItem>, val hasNext: Boolean) : ConditionalOrderUiState()
    data class TxSuccess(val message: String, val newOrderId: String? = null) : ConditionalOrderUiState()
    data class Error(val message: String) : ConditionalOrderUiState()
}

class ConditionalOrderViewModel : BaseViewModel() {
    private val apiService = TossNetworkClient.apiService

    private val _uiState = MutableStateFlow<ConditionalOrderUiState>(ConditionalOrderUiState.Loading)
    val uiState: StateFlow<ConditionalOrderUiState> = _uiState.asStateFlow()

    private val activeSchedulers = mutableListOf<ConditionalOrderHistoryItem>()
    private var nextCursor: String? = null

    override fun handleException(throwable: Throwable, message: String) {
        _uiState.value = ConditionalOrderUiState.Error(message)
    }

    /**
     * [감시망 수집] 실시간 활성화 상태인 감시 목록을 커서 페이징 결합하여 수신
     */
    fun loadSchedulers(status: String, isLoadMore: Boolean = false) {
        viewModelScope.launch(exceptionHandler) {
            if (!isLoadMore) {
                _uiState.value = ConditionalOrderUiState.Loading
                activeSchedulers.clear()
                nextCursor = null
            }
            // 계좌 식별 헤더 바인딩 보장
            TossAccountSession.refreshAndBindAccount(TossNetworkClient.apiService)

            val response = TossConditionalOrderHistoryRepository.fetchConditionalOrderHistory(
                status = status, nextPageCursor = if (isLoadMore) nextCursor else null
            )
            activeSchedulers.addAll(response.result.conditionalOrders)
            nextCursor = response.result.nextCursor
            _uiState.value = ConditionalOrderUiState.ListSuccess(activeSchedulers.toList(), response.result.hasNext)
        }
    }

    /**
     * [복합 예약 생성] OCO / OTO 비즈니스 제약 가드를 사전 통과한 건만 서버 전송
     */
    fun registerConditionalOrder(request: TossConditionalOrderCreateRequest) {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = ConditionalOrderUiState.Loading
            
            // 계좌 식별 헤더 바인딩 보장
            TossAccountSession.refreshAndBindAccount(TossNetworkClient.apiService)

            TossConditionalOrderGuard.validateConditionalOrder(
                type = request.type, orderType = request.orderType, first = request.first, second = request.second
            )
            val response = apiService.createConditionalOrder(request)
            _uiState.value = ConditionalOrderUiState.TxSuccess("조건주문이 정상 등록되었습니다.", response.result.conditionalOrderId)
            loadSchedulers("OPEN")
        }
    }

    /**
     * [Constraint Substitution] 수정 성공 시 가변 발급되는 신규 ID로 포인터를 대체 처리
     */
    fun modifyConditionalOrder(oldOrderId: String, request: TossConditionalOrderModifyRequest) {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = ConditionalOrderUiState.Loading
            
            TossConditionalOrderGuard.validateConditionalOrder(
                type = request.type, orderType = request.orderType, first = request.first, second = request.second
            )
            // 서버 원격 수정 집행 트리거
            val response = apiService.modifyConditionalOrder(oldOrderId, request)
            val newOrderId = response.result.conditionalOrderId
            
            _uiState.value = ConditionalOrderUiState.TxSuccess("스케줄러 수정 완료 (기존 감시망 무효화)", newOrderId)
            loadSchedulers("OPEN")
        }
    }

    /**
     * [HTTP 204 방어 취소] Body 가 비어있는 No Content 응답 규격을 통제 해제
     */
    fun terminateScheduler(conditionalOrderId: String) {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = ConditionalOrderUiState.Loading
            
            val response = apiService.cancelConditionalOrder(conditionalOrderId)
            if (response.code() == 204) { // 정상 처리 안전 락 검증
                _uiState.value = ConditionalOrderUiState.TxSuccess("자동 매매 감시망이 해제되었습니다.")
                loadSchedulers("OPEN")
            } else {
                _uiState.value = ConditionalOrderUiState.Error("서버 거부 응답 코드 수신")
            }
        }
    }
}
