package kr.hnu.ice.tossapplication

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
        
        // 정적 이벤트를 통한 자산 정보 및 인터랙션 액션 처리 설정
        binding.tvAccountView.setOnClickListener {
            Toast.makeText(requireContext(), "전체 계좌 레이어로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.itemSamsung.setOnClickListener {
            Toast.makeText(requireContext(), "삼성전자 상세 차트 레이어로 이동합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}