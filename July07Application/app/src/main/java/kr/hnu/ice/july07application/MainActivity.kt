package kr.hnu.ice.july07application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.julymyapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toSecondBtn.setOnClickListener {
            startActivity(Intent(this, SecondMainActivity::class.java))
        }
        binding.toThirdBtn.setOnClickListener {
            startActivity(Intent(this, ThirdActivity::class.java))
        }
        binding.toFourthBtn.setOnClickListener {
            startActivity(Intent(this, FourthActivity::class.java))
        }
    }
}