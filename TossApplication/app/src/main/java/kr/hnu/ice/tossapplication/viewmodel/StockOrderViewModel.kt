package kr.hnu.ice.tossapplication.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.networking.TossOrderCreateRequest
import kr.hnu.ice.tossapplication.networking.TossAccountSession
import kr.hnu.ice.tossapplication.repository.TossPreOrderChecker
import java.math.BigDecimal

sealed class OrderUiState {
    object Idle : OrderUiState()
    object Loading : OrderUiState()
    data class Success(val orderId: String) : OrderUiState()
    data class Error(val message: String) : OrderUiState()
}

class StockOrderViewModel : BaseViewModel() {
    private val apiService = TossNetworkClient.apiService

    private val _orderState = MutableStateFlow<OrderUiState>(OrderUiState.Idle)
    val orderState: StateFlow<OrderUiState> = _orderState.asStateFlow()

    override fun handleException(throwable: Throwable, message: String) {
        _orderState.value = OrderUiState.Error(message)
    }

    fun submitOrder(
        symbol: String,
        price: String,
        quantity: String,
        side: String,
        isUsStock: Boolean
    ) {
        viewModelScope.launch(exceptionHandler) {
            _orderState.value = OrderUiState.Loading

            // 계좌 식별 헤더 바인딩 보장
            TossAccountSession.refreshAndBindAccount(apiService)

            val orderQty = BigDecimal(quantity)
            val orderPrice = BigDecimal(price)
            val totalAmount = orderQty.multiply(orderPrice)
            val currency = if (isUsStock) "USD" else "KRW"

            // 1. 사전 검증 (Pre-Order Check)
            if (side == "BUY") {
                val hasPower = TossPreOrderChecker.verifyAssetBuyingPower(currency, totalAmount)
                if (!hasPower) {
                    _orderState.value = OrderUiState.Error("예수금이 부족합니다.")
                    return@launch
                }
            } else {
                val hasQty = TossPreOrderChecker.verifyAssetSellableQuantity(symbol, orderQty)
                if (!hasQty) {
                    _orderState.value = OrderUiState.Error("매도 가능한 수량이 부족합니다.")
                    return@launch
                }
            }

            // 2. 주문 실행
            val request = TossOrderCreateRequest(
                symbol = symbol,
                side = side,
                orderType = "LIMIT",
                quantity = quantity,
                price = price
            )
            val response = apiService.createOrder(request)
            
            _orderState.value = OrderUiState.Success(response.result.orderId)
        }
    }
}
