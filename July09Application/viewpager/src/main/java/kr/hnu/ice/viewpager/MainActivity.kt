package kr.hnu.ice.viewpager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.hnu.ice.viewpager.databinding.ActivityMainBinding

// 어댑터 구조 수정: 인스턴스를 미리 생성하지 않고 호출 시점에 동적 생성하도록 변경
class MyFragmentPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BlankFragment()
            1 -> BlankFragment2()
            2 -> BlankFragment3()
            else -> BlankFragment()
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Edge-to-Edge 활성화에 따른 상단바/하단바 겹침 방지 인셋 처리
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 뷰페이저에 어댑터 연결
        binding.viewPager.adapter = MyFragmentPagerAdapter(this)
    }
}