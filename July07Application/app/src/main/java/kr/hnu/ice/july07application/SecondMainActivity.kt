package kr.hnu.ice.july07application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.julymyapplication.databinding.ActivitySecondMainBinding

class SecondMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySecondMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toMainBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.toThirdBtn.setOnClickListener {
            startActivity(Intent(this, ThirdActivity::class.java))
        }
        binding.toFourthBtn.setOnClickListener {
            startActivity(Intent(this, FourthActivity::class.java))
        }
        binding.finish2nd.setOnClickListener {
            finish()
        }
    }
}