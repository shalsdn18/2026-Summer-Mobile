package kr.hnu.ice.picker

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.picker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // [초기 구동 상태 조율] 날짜 선택 활성화 기준 동기화 (image_be7e21.jpg 구조)
        binding.datePicker.visibility = View.VISIBLE
        binding.timePicker.visibility = View.INVISIBLE
        binding.dateBtn.isEnabled = false
        binding.timeBtn.isEnabled = true

        // 날짜 선택 버튼 이벤트 핸들러
        binding.dateBtn.setOnClickListener {
            binding.datePicker.visibility = View.VISIBLE
            binding.timePicker.visibility = View.INVISIBLE
            binding.dateBtn.isEnabled = false
            binding.timeBtn.isEnabled = true
        }

        // 시간 선택 버튼 이벤트 핸들러 (image_be7e05.jpg 구조로 스위칭)
        binding.timeBtn.setOnClickListener {
            binding.datePicker.visibility = View.INVISIBLE
            binding.timePicker.visibility = View.VISIBLE
            binding.dateBtn.isEnabled = true
            binding.timeBtn.isEnabled = false
        }
    }
}