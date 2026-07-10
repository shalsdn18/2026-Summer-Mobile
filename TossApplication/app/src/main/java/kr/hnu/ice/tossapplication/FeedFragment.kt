package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kr.hnu.ice.tossapplication.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private var isLiked = false

    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 팔로우 상호작용 채널 연결
        binding.btnFollow1.setOnClickListener {
            binding.btnFollow1.text = "팔로잉"
            binding.btnFollow1.setTextColor(android.graphics.Color.parseColor("#8B95A1"))
            Toast.makeText(requireContext(), "Katoo님을 팔로우합니다.", Toast.LENGTH_SHORT).show()
        }

        // 2. 소셜 카운터 좋아요 증감 연산 처리
        binding.tvLikeCount.setOnClickListener {
            if (!isLiked) {
                binding.tvLikeCount.text = "♥ 72"
                binding.tvLikeCount.setTextColor(android.graphics.Color.parseColor("#F04452"))
                isLiked = true
            } else {
                binding.tvLikeCount.text = "♥ 71"
                binding.tvLikeCount.setTextColor(android.graphics.Color.parseColor("#4E5968"))
                isLiked = false
            }
        }

        // 3. 플로팅 글쓰기 에디터 컴포넌트 링크
        binding.fabWritePost.setOnClickListener {
            Toast.makeText(requireContext(), "새 커뮤니티 에디터 창구를 오픈합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}