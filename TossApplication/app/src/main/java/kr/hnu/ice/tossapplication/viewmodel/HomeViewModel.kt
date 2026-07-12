package kr.hnu.ice.tossapplication.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.networking.HoldingStockItem
import kr.hnu.ice.tossapplication.networking.TossAccountSession
import java.util.Locale

// [구조 확장] CDN 식별용 ISIN 코드가 포함된 UI 전용 확장 데이터 모델
data class EnrichedHoldingStock(
    val symbol: String,
    val name: String,
    val marketCountry: String,
    val quantity: String,
    val evaluationAmountKrw: String,
    val profitLossAmount: String,
    val profitLossRate: String,
    val isinCode: String, // CDN 맵 타겟 키
    val currency: String
)

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val totalEvaluation: String,
        val totalProfitLossRate: String,
        val totalProfitLossAmount: String,
        val krwCash: String,
        val usdCash: String,
        val openOrdersCount: Int,
        val domesticStocks: List<EnrichedHoldingStock>,
        val overseasStocks: List<EnrichedHoldingStock>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel : BaseViewModel() {
    private val apiService = TossNetworkClient.apiService

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchLiveAssetDashboard()
    }

    override fun handleException(throwable: Throwable, message: String) {
        _uiState.value = HomeUiState.Error(message)
    }

    fun fetchLiveAssetDashboard() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = HomeUiState.Loading

            // 0. 계좌 식별 헤더 바인딩 보장 (토큰 및 예수금 정보 갱신 보장)
            TossAccountSession.refreshAndBindAccount(apiService)

            // 1. 자산 잔고와 미체결 대기 주문 내역 병렬 수집
            val holdingsDeferred = async { apiService.getAssetHoldings() }
            val openOrdersDeferred = async { apiService.getOrderHistoryList(status = "OPEN") }

            val holdingsRes = holdingsDeferred.await()
            val openOrdersRes = openOrdersDeferred.await()

            val resultData = holdingsRes.result
            val allStocks = resultData.items

            // 2. 보유 종목들의 symbol 전체 취합 후 Stock Info API 다건 조회 가동 (ISIN 코드 획득 목적)
            val symbolsQuery = allStocks.map { it.symbol }.joinToString(",")
            val stockInfoMap = if (symbolsQuery.isNotEmpty()) {
                try {
                    val infoRes = apiService.getStockInfo(symbolsQuery)
                    infoRes.result.associateBy { it.symbol }
                } catch (e: Exception) {
                    emptyMap()
                }
            } else {
                emptyMap()
            }

            // 3. 국적 분류 및 ISIN 코드 동적 오버레이 결합
            val domesticList = allStocks.filter { it.marketCountry == "KR" }.map { item ->
                val metadata = stockInfoMap[item.symbol]
                EnrichedHoldingStock(
                    symbol = item.symbol,
                    name = item.name,
                    marketCountry = item.marketCountry,
                    quantity = item.quantity,
                    evaluationAmountKrw = item.itemMarketValue.amount,
                    profitLossAmount = item.itemProfitLoss.amount,
                    profitLossRate = item.itemProfitLoss.rate,
                    isinCode = metadata?.isinCode ?: "",
                    currency = item.currency
                )
            }

            val overseasList = allStocks.filter { it.marketCountry == "US" }.map { item ->
                val metadata = stockInfoMap[item.symbol]
                EnrichedHoldingStock(
                    symbol = item.symbol,
                    name = item.name,
                    marketCountry = item.marketCountry,
                    quantity = item.quantity,
                    evaluationAmountKrw = item.itemMarketValue.amount,
                    profitLossAmount = item.itemProfitLoss.amount,
                    profitLossRate = item.itemProfitLoss.rate,
                    isinCode = metadata?.isinCode ?: "",
                    currency = item.currency
                )
            }

            // =================================================================
            // [정정 핵심] 실화면 기준 달러 환율 변수 선언 및 크로스 자산 합산 연산
            // =================================================================
            val exchangeRate = 1504.80

            val domesticEval = resultData.marketValue.amount.krw.replace(",", "").toDoubleOrNull() ?: 0.0
            val overseasEval = resultData.marketValue.amount.usd.replace(",", "").toDoubleOrNull() ?: 0.0
            // 진짜 총 투자 평가액 도출 (국내 + 해외 * 환율)
            val trueTotalEvaluation = (domesticEval + (overseasEval * exchangeRate)).toLong().toString()

            val domesticProfit = resultData.profitLoss.amount.krw.replace(",", "").toDoubleOrNull() ?: 0.0
            val overseasProfit = resultData.profitLoss.amount.usd.replace(",", "").toDoubleOrNull() ?: 0.0
            // 진짜 총 손익금 도출 (국내 + 해외 * 환율)
            val trueTotalProfitAmount = (domesticProfit + (overseasProfit * exchangeRate)).toLong().toString()

            // 소수점 비율 스케일 가드 정정 (0.2754 -> 27.5)
            val rawRate = resultData.profitLoss.rate.toDoubleOrNull() ?: 0.0
            val scaledRateString = String.format(Locale.KOREA, "%.1f", rawRate * 100)

            _uiState.value = HomeUiState.Success(
                totalEvaluation = trueTotalEvaluation,
                totalProfitLossRate = scaledRateString,
                totalProfitLossAmount = trueTotalProfitAmount,
                krwCash = TossAccountSession.getKrwCash(), // 세션 내부 독립 원화 예수금 주입
                usdCash = TossAccountSession.getUsdCash(), // 세션 내부 독립 달러 예수금 주입
                openOrdersCount = openOrdersRes.result.orders.size,
                domesticStocks = domesticList,
                overseasStocks = overseasList
            )
        }
    }
}
