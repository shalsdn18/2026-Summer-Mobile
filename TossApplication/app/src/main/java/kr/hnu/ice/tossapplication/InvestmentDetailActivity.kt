package kr.hnu.ice.tossapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityInvestmentDetailBinding

class InvestmentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvestmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvestmentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 뒤로가기 화살표 터치 시 현재 메모리 스택에서 파괴 반환
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnProportionBanner.setOnClickListener {
            val intent = Intent(this, AssetProportionActivity::class.java)
            startActivity(intent)
        }
    }
}