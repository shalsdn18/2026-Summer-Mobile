package kr.hnu.ice.tossapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityMoreMenuBinding

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
    }
}