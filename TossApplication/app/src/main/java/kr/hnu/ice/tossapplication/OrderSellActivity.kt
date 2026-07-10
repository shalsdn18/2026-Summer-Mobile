package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityOrderSellBinding

class OrderSellActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderSellBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSellBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnOrderAction.setOnClickListener {
            Toast.makeText(this, "매도 주문이 정상 접수되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}