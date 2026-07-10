package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kr.hnu.ice.tossapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tabs: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 일괄 텍스트 제어를 위한 배열 구성
        tabs = listOf(binding.btnNavHome, binding.btnNavWatch, binding.btnNavDiscover, binding.btnNavFeed)

        // 1. 최초 앱 구동 시 기본 화면 세팅 (증권 홈 프래그먼트)
        switchFragment(HomeFragment(), binding.btnNavHome)

        // 2. 고정 네비게이션 버튼 각각의 전환 이벤트 바인딩
        binding.btnNavHome.setOnClickListener {
            switchFragment(HomeFragment(), binding.btnNavHome)
        }

        binding.btnNavDiscover.setOnClickListener {
            switchFragment(DiscoveryFragment(), binding.btnNavDiscover)
        }

        binding.btnNavWatch.setOnClickListener {
            switchFragment(WatchFragment(), binding.btnNavWatch)
        }

        binding.btnNavFeed.setOnClickListener {
            switchFragment(FeedFragment(), binding.btnNavFeed)
        }

        // 3. 하단 시스템 좌측 화살표 버튼 클릭 시 종료 스택 처리
        binding.btnNavBack.setOnClickListener {
            finish()
        }
    }

    private fun switchFragment(fragment: Fragment, targetButton: Button) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        updateTabStyle(targetButton)
    }

    // 활성화된 탭 글자 강조 최적화 로직
    private fun updateTabStyle(selectedButton: Button) {
        tabs.forEach { button ->
            if (button == selectedButton) {
                button.setTextColor(android.graphics.Color.parseColor("#191F28")) // 활성: Dark
                button.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                button.setTextColor(android.graphics.Color.parseColor("#8B95A1")) // 비활성: Gray
                button.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }
}