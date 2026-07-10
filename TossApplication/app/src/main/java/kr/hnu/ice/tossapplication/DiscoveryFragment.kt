package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kr.hnu.ice.tossapplication.databinding.FragmentDiscoveryBinding

class DiscoveryFragment : Fragment() {
    private var _binding: FragmentDiscoveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 실시간 매크로 이슈 알림 패널 상호작용
        binding.layoutRealtimeBanner.setOnClickListener {
            Toast.makeText(requireContext(), "반도체 테마 실시간 스크리닝 뉴스로 이동", Toast.LENGTH_SHORT).show()
        }

        // 2. 리스트 내 개별 종목 행 터치 시 인텐트 라우팅 연동
        binding.rankItem1.setOnClickListener {
            val intent = android.content.Intent(requireContext(), StockDetailActivity::class.java)
            startActivity(intent)
        }

        // 3. 중간 인젝션 커뮤니티 전용 카드 이벤트 바인딩
        binding.cardCommunityInjected.setOnClickListener {
            Toast.makeText(requireContext(), "실시간 토론방 급상승 스레드로 스위칭", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}