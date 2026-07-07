package kr.hnu.ice.july07application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.julymyapplication.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toMainBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.toSecondBtn.setOnClickListener {
            startActivity(Intent(this, SecondMainActivity::class.java))
        }
        binding.toFourthBtn.setOnClickListener {
            startActivity(Intent(this, FourthActivity::class.java))
        }
        binding.finish3rd.setOnClickListener {
            finish()
        }
    }
}