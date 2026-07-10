package kr.hnu.ice.tossapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kr.hnu.ice.tossapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        
        // =================================================================
        // [1] 우측 상단 액션바 메뉴 리스너 완전 통합 제어 구문
        // =================================================================
        
        // 제미나이 AI 버튼 복구 (AiSignalActivity 연동)
        binding.btnMenuGemini.setOnClickListener {
            val intent = Intent(requireContext(), AiSignalActivity::class.java).apply {
                // 기존 스택에 액티비티가 있다면 재사용하고 상위 스택을 클리어하여 메모리 오버헤드 방지
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            // 화면 전환 애니메이션으로 인한 렉을 방지하기 위해 정적 전환 처리
            activity?.overridePendingTransition(0, 0)
        }

        // 검색 버튼 복구 (SearchActivity 연동)
        binding.btnMenuSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            activity?.overridePendingTransition(0, 0)
        }

        // 더보기 버튼 복구 (레드닷 알림 가드 포함)
        binding.btnMenuMore.setOnClickListener {
            binding.viewRedDot.visibility = View.GONE 
            val intent = Intent(requireContext(), MoreMenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            activity?.overridePendingTransition(0, 0)
        }

        // =================================================================
        // [2] 기존 본문 주동 컴포넌트 인터랙션 리스너 가동 채널
        // =================================================================
        
        // 계좌보기 버튼 클릭 시
        binding.tvAccountView.setOnClickListener {
            val intent = Intent(requireContext(), AccountActivity::class.java)
            startActivity(intent)
        }

        // 내 투자 타이틀 영역 클릭 시 (InvestmentDetailActivity 연동)
        binding.tvInvestmentLabel.setOnClickListener {
            val intent = Intent(requireContext(), InvestmentDetailActivity::class.java)
            startActivity(intent)
        }

        // 자산 금액 텍스트 클릭 시 (InvestmentDetailActivity 연동)
        binding.tvTotalAssetValue.setOnClickListener {
            val intent = Intent(requireContext(), InvestmentDetailActivity::class.java)
            startActivity(intent)
        }

        // 국내주식: 삼성전자 클릭 시 (StockDetailActivity 연동)
        binding.itemSamsung.setOnClickListener {
            val intent = Intent(requireContext(), StockDetailActivity::class.java)
            startActivity(intent)
        }

        // =================================================================
        // [3] 기타 더미 하위 컴포넌트 피드백 채널
        // =================================================================
        binding.itemMicron.setOnClickListener {
            Toast.makeText(requireContext(), "마이크론 테크놀로지 분석 레이어로 이동", Toast.LENGTH_SHORT).show()
        }

        binding.itemNvidia.setOnClickListener {
            Toast.makeText(requireContext(), "엔비디아 상세 차트 레이어로 이동", Toast.LENGTH_SHORT).show()
        }

        binding.cardAnalysis.setOnClickListener {
            Toast.makeText(requireContext(), "이번 달 상세 수익분석 리포트 구동", Toast.LENGTH_SHORT).show()
        }

        binding.chartItem1.setOnClickListener {
            Toast.makeText(requireContext(), "SK하이닉스 레버리지 주문 바텀시트 호출", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}