package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityOrderBuyBinding

class OrderBuyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBuyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnOrderAction.setOnClickListener {
            Toast.makeText(this, "매수 주문이 정상 접수되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}