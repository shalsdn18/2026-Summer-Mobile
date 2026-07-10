package kr.hnu.ice.tossapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityStockDetailBinding

class StockDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 뒤로가기 화살표 이벤트 결합
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 하단 핵심 트레이딩 액션 바인딩
        binding.btnSell.setOnClickListener {
            val intent = Intent(this, OrderSellActivity::class.java)
            startActivity(intent)
        }

        binding.btnBuy.setOnClickListener {
            val intent = Intent(this, OrderBuyActivity::class.java)
            startActivity(intent)
        }
    }
}