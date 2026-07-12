package kr.hnu.ice.tossapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityMoreMenuBinding
import kr.hnu.ice.tossapplication.networking.TossSessionManager

class MoreMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoreMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 툴바 ← 뒤로가기 버튼 액션 스택 종료
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnToolbarSearch.setOnClickListener {
            Toast.makeText(this, "통합 종목 검색 레이어로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnToolbarSettings.setOnClickListener {
            Toast.makeText(this, "개인 맞춤 보안 환경설정 패널을 구동합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.layoutStockAccumulateRow.setOnClickListener {
            // 주식 모으기 행 터치 시 정기 적립 상세 대시보드로 인텐트 라우팅 가동
            val intent = Intent(this, StockAccumulateActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            // 1. 전역 세션 및 캐시 파기
            TossSessionManager.performLogout()
            
            // 2. 인증 화면으로 강제 이동 및 스택 클리어
            val intent = Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
