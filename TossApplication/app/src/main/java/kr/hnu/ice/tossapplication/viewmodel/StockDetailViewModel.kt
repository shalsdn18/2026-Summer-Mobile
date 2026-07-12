package kr.hnu.ice.tossapplication.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.networking.OrderbookResult
import kr.hnu.ice.tossapplication.networking.PriceItem

sealed class StockDetailUiState {
    object Loading : StockDetailUiState()
    data class Success(
        val priceInfo: PriceItem,
        val orderbook: OrderbookResult
    ) : StockDetailUiState()
    data class Error(val message: String) : StockDetailUiState()
}

class StockDetailViewModel : BaseViewModel() {
    private val apiService = TossNetworkClient.apiService

    private val _uiState = MutableStateFlow<StockDetailUiState>(StockDetailUiState.Loading)
    val uiState: StateFlow<StockDetailUiState> = _uiState.asStateFlow()

    override fun handleException(throwable: Throwable, message: String) {
        _uiState.value = StockDetailUiState.Error(message)
    }

    /**
     * [Data Concurrency] 호가 정보와 현재가 상세 스냅샷을 병렬 아싱크(Async) 트리거로 가속 수집
     */
    fun loadStockMarketData(symbol: String) {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = StockDetailUiState.Loading
            
            // 토큰 유효성 보장
            ensureAuthenticated()

            val orderbookDeferred = async { apiService.getOrderbook(symbol) }
            val priceDeferred = async { apiService.getPrices(symbol) }

            val orderbookRes = orderbookDeferred.await()
            val priceRes = priceDeferred.await()

            val priceItem = priceRes.result.firstOrNull { it.symbol == symbol }
                ?: throw NoSuchElementException("종목 단건 매칭 데이터 실패")

            _uiState.value = StockDetailUiState.Success(
                priceInfo = priceItem,
                orderbook = orderbookRes.result
            )
        }
    }
}
