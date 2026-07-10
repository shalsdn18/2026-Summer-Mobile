package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityAiSignalBinding

class AiSignalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAiSignalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiSignalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 뒤로가기 버튼 처리
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 소개 카드별 테스트 클릭 피드백 구성
        binding.cardSignal.setOnClickListener {
            Toast.makeText(this, "실시간 변동 시그널 명세 레이어로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.cardEarningCall.setOnClickListener {
            Toast.makeText(this, "글로벌 CEO 실시간 음성 어닝콜 스트리밍 채널에 접속합니다.", Toast.LENGTH_SHORT).show()
        }
    }
}