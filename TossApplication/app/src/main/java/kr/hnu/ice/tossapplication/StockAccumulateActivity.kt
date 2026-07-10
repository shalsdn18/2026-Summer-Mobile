package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityStockAccumulateBinding

class StockAccumulateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockAccumulateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockAccumulateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 백 버튼 스택 클리어 리스너
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSettings.setOnClickListener {
            Toast.makeText(this, "적립 주기 및 자동 이체 상세 설정으로 이동", Toast.LENGTH_SHORT).show()
        }

        // 최하단 액션 코어 연동 가드
        binding.btnStockAdd.setOnClickListener {
            Toast.makeText(this, "정기 모으기 신규 주식 스크리너를 구동합니다.", Toast.LENGTH_SHORT).show()
        }
    }
}