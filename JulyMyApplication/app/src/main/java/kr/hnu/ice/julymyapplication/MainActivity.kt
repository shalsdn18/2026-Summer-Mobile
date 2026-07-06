package kr.hnu.ice.julymyapplication

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.addCallback // 1. addCallback 람다 구현을 위한 필수 임포트
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.julymyapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 스톱워치 경과 시간 오프셋 변수
    var elapsedtime = 0L

    // 2. 뒤로가기 누른 시점을 기록할 멤버 변수 정의
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 객체 인플레이트
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 코틀린 프로퍼티 문법으로 활성화 상태 통일
        binding.startBtn.isEnabled = true
        binding.stopBtn.isEnabled = false

        // 1. 시작 버튼 클릭
        binding.startBtn.setOnClickListener {
            binding.chronometer.base = SystemClock.elapsedRealtime() + elapsedtime
            binding.startBtn.isEnabled = false
            binding.stopBtn.isEnabled = true
            binding.chronometer.start()
        }

        // 2. 중지 버튼 클릭
        binding.stopBtn.setOnClickListener {
            elapsedtime = binding.chronometer.base - SystemClock.elapsedRealtime()

            binding.stopBtn.isEnabled = false
            binding.startBtn.isEnabled = true
            binding.chronometer.stop()

            Toast.makeText(this, "경과 시간: ${binding.chronometer.text}", Toast.LENGTH_SHORT).show()
        }

        // 3. 초기화 버튼 클릭
        binding.resetBtn.setOnClickListener {
            elapsedtime = 0L
            binding.chronometer.base = SystemClock.elapsedRealtime()

            // 초기화 시 버튼 및 타이머 상태 리셋
            binding.startBtn.isEnabled = true
            binding.stopBtn.isEnabled = false
            binding.chronometer.stop()
        }

        // 4. 뒤로가기 콜백 구현 교정 (image_71dd24.png 원본 스펙 적용)
        onBackPressedDispatcher.addCallback(this) {
            val currentTime = System.currentTimeMillis()

            if (currentTime - backPressedTime < 5000) {
                // 5초 이내에 다시 누른 경우 앱 종료 프로세스 실행
                finish()
            } else {
                // 최초 누름 혹은 2초가 지난 경우 안내 문구 출력 후 시간 업데이트
                Toast.makeText(this@MainActivity, "종료하려면 한 번 더 누르세요!!", Toast.LENGTH_SHORT).show()
                backPressedTime = currentTime
            }
        }
    }
}