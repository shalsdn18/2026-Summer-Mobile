package kr.hnu.ice.tossapplication.view

import android.graphics.Color
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
import kr.hnu.ice.tossapplication.databinding.ActivityHomeBinding
import kr.hnu.ice.tossapplication.viewmodel.HomeUiState
import kr.hnu.ice.tossapplication.viewmodel.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var domesticAdapter: HoldingStockAdapter
    private lateinit var overseasAdapter: HoldingStockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapterConfiguration()
        observeAssetDashboardStream()

        // 실시간 자산 대시보드 갱신 구동
        viewModel.fetchLiveAssetDashboard()
    }

    private fun initAdapterConfiguration() {
        domesticAdapter = HoldingStockAdapter { symbol ->
            // 상세 페이지 이동 로직
        }
        overseasAdapter = HoldingStockAdapter { symbol ->
            // 상세 페이지 이동 로직
        }

        // 국내주식 리사이클러뷰 바인딩
        binding.rvDomesticStocks.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = domesticAdapter
            isNestedScrollingEnabled = false
        }

        // 해외주식 리사이클러뷰 바인딩
        binding.rvOverseasStocks.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = overseasAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun observeAssetDashboardStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is HomeUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is HomeUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            
                            // 1. 상단 배지 노드 텍스트 바인딩
                            binding.tvPendingOrdersBadge.text = "해외주식 ${state.openOrdersCount}건"
                            binding.tvKrwCashBadge.text = "${formatCurrency(state.krwCash)}원"
                            binding.tvUsdCashBadge.text = "$${formatCurrency(state.usdCash)}"

                            // 2. 내 투자 자산 총액 및 등락 판넬 동적 컬러링
                            binding.tvTotalInvestment.text = "${formatCurrency(state.totalEvaluation)}원"
                            binding.tvTotalProfitLoss.text = "${formatCurrency(state.totalProfitLossAmount)}원 (${state.totalProfitLossRate}%)"
                            
                            // 토스 고유의 등락 색상 가드 정의 (양수: 레드, 음수: 블루)
                            val colorRes = if (state.totalProfitLossRate.startsWith("-")) {
                                Color.parseColor("#3182F6") // 토스 블루
                            } else if (state.totalProfitLossRate != "0" && state.totalProfitLossRate != "0.0") {
                                Color.parseColor("#E52528") // 토스 레드
                            } else {
                                Color.parseColor("#191F28")
                            }
                            binding.tvTotalProfitLoss.setTextColor(colorRes)

                            // 3. 시장 국적별 어댑터 데이터 덤프 분리 로드
                            domesticAdapter.submitList(state.domesticStocks)
                            overseasAdapter.submitList(state.overseasStocks)
                        }
                        is HomeUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@HomeActivity, state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun formatCurrency(value: String): String {
        return try {
            val cleaned = value.replace(",", "")
            val parsed = cleaned.toDouble()
            if (parsed % 1 == 0.0) {
                NumberFormat.getInstance(Locale.KOREA).format(parsed.toLong())
            } else {
                String.format("%.2f", parsed)
            }
        } catch (e: Exception) {
            value
        }
    }
}
