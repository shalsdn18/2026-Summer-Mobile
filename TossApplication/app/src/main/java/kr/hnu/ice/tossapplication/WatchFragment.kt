package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kr.hnu.ice.tossapplication.databinding.FragmentWatchBinding

class WatchFragment : Fragment() {
    private var _binding: FragmentWatchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 관심 목록 가상 삭제 액션 처리 리스너 연동
        binding.btnDelete1.setOnClickListener {
            binding.watchItem1.visibility = View.GONE
            Toast.makeText(requireContext(), "관심 종목이 해제되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnDelete2.setOnClickListener {
            binding.watchItem2.visibility = View.GONE
        }

        binding.layoutNewsItem1.setOnClickListener {
            Toast.makeText(requireContext(), "선택한 AI 매칭 인라인 뉴스로 이동합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}