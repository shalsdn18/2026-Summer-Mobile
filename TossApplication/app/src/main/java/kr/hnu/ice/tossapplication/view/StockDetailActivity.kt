package kr.hnu.ice.tossapplication.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kr.hnu.ice.tossapplication.databinding.ActivityStockDetailBinding
import kr.hnu.ice.tossapplication.viewmodel.*

class StockDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockDetailBinding
    
    // 시세 공급 및 주문 집행용 듀얼 뷰모델 바인딩
    private val detailViewModel: StockDetailViewModel by viewModels()
    private val orderViewModel: StockOrderViewModel by viewModels()
    
    private lateinit var orderbookAdapter: OrderbookAdapter
    private var currentSymbol: String = "005930" // 기본값: 삼성전자
    private var isUsStock: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 가상 인텐트 데이터 바인딩 파싱 가드
        currentSymbol = intent.getStringExtra("EXTRA_SYMBOL") ?: "005930"
        isUsStock = currentSymbol.matches(Regex("^[A-Za-z]+$")) // 영문 티커 가드 판별

        initViews()
        observeDataStreams()

        // 최초 시세 로드 실행
        detailViewModel.loadStockMarketData(currentSymbol)
    }

    private fun initViews() {
        orderbookAdapter = OrderbookAdapter()
        binding.rvOrderbook.apply {
            layoutManager = LinearLayoutManager(this@StockDetailActivity)
            adapter = orderbookAdapter
        }

        // [살기] 버튼 누르면 임시 지정가 매수 트랜잭션 구동 (실제 앱에서는 수량 입력 팝업 연결)
        binding.btnBuy.setOnClickListener {
            orderViewModel.submitOrder(
                symbol = currentSymbol,
                price = "70000",
                quantity = "10",
                side = "BUY",
                isUsStock = isUsStock
            )
        }

        // [팔기] 버튼 매도 구동
        binding.btnSell.setOnClickListener {
            orderViewModel.submitOrder(
                symbol = currentSymbol,
                price = "70000",
                quantity = "5",
                side = "SELL",
                isUsStock = isUsStock
            )
        }
    }

    /**
     * [Multi-Stream Collection] 두 개의 독립된 상태 스트림을 원자적으로 분할 추적
     */
    private fun observeDataStreams() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 1. 실시간 시세 및 호가판 스냅샷 트래킹 수집
                launch {
                    detailViewModel.uiState.collect { state ->
                        when (state) {
                            is StockDetailUiState.Loading -> { /* 로딩 인디케이터 제어 */ }
                            is StockDetailUiState.Success -> {
                                binding.tvStockName.text = if (isUsStock) state.priceInfo.symbol else "국내 주식"
                                binding.tvStockSymbol.text = state.priceInfo.symbol
                                binding.tvCurrentPrice.text = "${state.priceInfo.lastPrice} ${state.priceInfo.currency}"
                                
                                // 호가 리사이클러뷰 어댑터에 매도/매수 리스트 결합 정렬 주입
                                orderbookAdapter.updateData(state.orderbook.asks, state.orderbook.bids)
                            }
                            is StockDetailUiState.Error -> {
                                Toast.makeText(this@StockDetailActivity, state.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                // 2. 주문 실행 결과 트랜잭션 상태 수집 (TossOrderValidationGuard 검증 피드백 처리 계층)
                launch {
                    orderViewModel.orderState.collect { state ->
                        when (state) {
                            is OrderUiState.Loading -> {
                                binding.btnBuy.isEnabled = false
                                binding.btnSell.isEnabled = false
                            }
                            is OrderUiState.Success -> {
                                binding.btnBuy.isEnabled = true
                                binding.btnSell.isEnabled = true
                                Toast.makeText(this@StockDetailActivity, "주문 성공! ID: ${state.orderId}", Toast.LENGTH_LONG).show()
                            }
                            is OrderUiState.Error -> {
                                binding.btnBuy.isEnabled = true
                                binding.btnSell.isEnabled = true
                                // 예수금 부족 가드 및 규격 위반 텍스트 표출 가동
                                Toast.makeText(this@StockDetailActivity, state.message, Toast.LENGTH_LONG).show()
                            }
                            is OrderUiState.Idle -> {}
                        }
                    }
                }
            }
        }
    }
}
