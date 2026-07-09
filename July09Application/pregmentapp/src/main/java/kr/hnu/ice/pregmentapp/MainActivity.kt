package kr.hnu.ice.pregmentapp // fragmentapp -> pregmentapp으로 수정

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.pregmentapp.databinding.ActivityMainBinding // 패키지 경로 수정

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 상태 지정
        binding.firstBtn.isEnabled = false

        binding.firstBtn.setOnClickListener {
            binding.firstBtn.isEnabled = false
            binding.secondBtn.isEnabled = true
            binding.thirdBtn.isEnabled = true
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, FirstFragment())
                .commit()
        }

        binding.secondBtn.setOnClickListener {
            binding.firstBtn.isEnabled = true
            binding.secondBtn.isEnabled = false // 현재 버튼 비활성화
            binding.thirdBtn.isEnabled = true
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, SecondFragment())
                .commit()
        }

        binding.thirdBtn.setOnClickListener {
            binding.firstBtn.isEnabled = true
            binding.secondBtn.isEnabled = true
            binding.thirdBtn.isEnabled = false // 현재 버튼 비활성화
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, ThirdFragment())
                .commit()
        }
    }
}