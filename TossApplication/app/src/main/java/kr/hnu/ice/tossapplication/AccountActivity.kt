package kr.hnu.ice.tossapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 상단 화살표 오브젝트 클릭 시 스택 파괴
        binding.btnBack.setOnClickListener {
            moveToHome()
        }

        // 2. 시스템 하드웨어 뒤로가기/화면 제스처 대응 가드 추가
        onBackPressedDispatcher.addCallback(this, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveToHome()
            }
        })
    }

    private fun moveToHome() {
        finish() // 현재 액티비티를 종료하여 백스택에 살아있는 MainActivity(홈)를 전면에 노출
    }
}