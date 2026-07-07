package kr.hnu.ice.july07application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.julymyapplication.databinding.ActivityFourthBinding

class FourthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toMainBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.toSecondBtn.setOnClickListener {
            startActivity(Intent(this, SecondMainActivity::class.java))
        }
        binding.toThirdBtn.setOnClickListener {
            startActivity(Intent(this, ThirdActivity::class.java))
        }
        binding.finish4th.setOnClickListener {
            finish()
        }
    }
}