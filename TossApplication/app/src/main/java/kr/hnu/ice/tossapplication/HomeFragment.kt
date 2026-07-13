package kr.hnu.ice.tossapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import kr.hnu.ice.tossapplication.databinding.FragmentHomeBinding
import kr.hnu.ice.tossapplication.view.*
import kr.hnu.ice.tossapplication.viewmodel.HomeUiState
import kr.hnu.ice.tossapplication.viewmodel.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    // 비동기 예외 가드(BaseViewModel) 상속 구조 뷰모델 결합
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var domesticAdapter: HoldingStockAdapter
    private lateinit var overseasAdapter: HoldingStockAdapter

    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initAdapterConfiguration()
        initStaticClickListeners()
        observeLiveAssetStream()

        // 실시간 자산 대시보드 갱신 구동
        viewModel.fetchLiveAssetDashboard()
    }

    private fun initAdapterConfiguration() {
        // 아이템 클릭 시 해당 종목 코드를 인텐트에 결합하여 상세 화면으로 즉시 이관 처리
        val navigateToDetail: (String) -> Unit = { symbol ->
            val intent = Intent(requireContext(), StockDetailActivity::class.java).apply {
                putExtra("EXTRA_SYMBOL", symbol)
            }
            startActivity(intent)
        }

        domesticAdapter = HoldingStockAdapter(navigateToDetail)
        overseasAdapter = HoldingStockAdapter(navigateToDetail)

        binding.rvDomesticStocks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = domesticAdapter
        }

        binding.rvOverseasStocks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = overseasAdapter
        }
    }

    private fun initStaticClickListeners() {
        // =================================================================
        // [1] 우측 상단 액션바 메뉴 리스너 (기능 복구 및 피드백 강화)
        // =================================================================
        binding.btnMenuGemini.setOnClickListener {
            Toast.makeText(requireContext(), "AI 시그널 레이어를 호출합니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), AiSignalActivity::class.java)
            startActivity(intent)
        }

        binding.btnMenuSearch.setOnClickListener {
            Toast.makeText(requireContext(), "통합 검색 창구를 호출합니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnMenuMore.setOnClickListener {
            binding.viewRedDot.visibility = View.GONE 
            Toast.makeText(requireContext(), "주식 모으기 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), StockAccumulateActivity::class.java)
            startActivity(intent)
        }

        // =================================================================
        // [2] 본문 유동 계좌 및 수익분석 내역 연동 채널
        // =================================================================
        binding.tvAccountView.setOnClickListener {
            startActivity(Intent(requireContext(), AccountActivity::class.java))
        }

        val navigateToInvestmentDetail = {
            startActivity(Intent(requireContext(), InvestmentDetailActivity::class.java))
        }
        binding.tvInvestmentLabel.setOnClickListener { navigateToInvestmentDetail() }
        binding.tvTotalAssetValue.setOnClickListener { navigateToInvestmentDetail() }

        binding.cardAnalysis.setOnClickListener {
            startActivity(Intent(requireContext(), OrderHistoryActivity::class.java))
        }

        binding.chartItem1.setOnClickListener {
            Toast.makeText(requireContext(), "SK하이닉스 레버리지 주문 바텀시트 호출", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * [Stream Consumer] 뷰모델이 동시 발동시킨 병렬 연산 결과를 UI 스냅샷에 원자적으로 적용
     */
    private fun observeLiveAssetStream() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is HomeUiState.Loading -> { /* 메인 스레드 렉 방지용 프로그레스 바 제어 가능 */ }
                        is HomeUiState.Success -> {
                            // A. 2대 핵심 예수금 가상 카드 덤프 바인딩
                            binding.tvKrwCashValue.text = "${formatCurrency(state.krwCash)}원"
                            binding.tvUsdCashValue.text = "$${formatCurrency(state.usdCash)}"

                            // B. 내 투자 요약 마스터 패널 텍스트 및 등락률 컬러 인젝션
                            binding.tvTotalAssetValue.text = "${formatCurrency(state.totalEvaluation)}원 〉"
                            binding.tvTotalProfit.text = "${formatCurrency(state.totalProfitLossAmount)}원 (${state.totalProfitLossRate}%)"

                            val profitColor = if (state.totalProfitLossRate.startsWith("-")) {
                                Color.parseColor("#3182F6") // 블루
                            } else if (state.totalProfitLossRate != "0" && state.totalProfitLossRate != "0.0") {
                                Color.parseColor("#F04452") // 레드
                            } else {
                                Color.parseColor("#191F28")
                            }
                            binding.tvTotalProfit.setTextColor(profitColor)

                            // C. 리사이클러 어댑터에 연산 분류된 도메인 리스트 주입 주동 렌더링
                            domesticAdapter.submitList(state.domesticStocks)
                            overseasAdapter.submitList(state.overseasStocks)
                        }
                        is HomeUiState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
