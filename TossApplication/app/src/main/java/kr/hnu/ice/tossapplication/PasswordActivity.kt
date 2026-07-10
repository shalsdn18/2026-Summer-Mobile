package kr.hnu.ice.tossapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private var inputCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 화면에 배치된 10개의 숫자 입력용 버튼을 물리적 순서대로 리스트화
        val keyButtons = listOf(
            binding.key7, binding.key4, binding.key8,
            binding.key6, binding.key5, binding.key3,
            binding.key9, binding.key2, binding.key0,
            binding.key1
        )

        // 2. [핵심] 0부터 9까지의 숫자를 무작위로 섞은 리스트 생성 (열릴 때마다 변경됨)
        val shuffledNumbers = (0..9).toList().shuffled()

        // 3. 버튼 객체와 난수 데이터를 1:1 매핑하여 글자 주입 및 이벤트 바인딩
        keyButtons.forEachIndexed { index, button ->
            val targetNumber = shuffledNumbers[index]
            button.text = targetNumber.toString() // 버튼 겉면 글자 변경

            button.setOnClickListener {
                // 어떤 숫자가 눌렸는지 확인이 필요한 경우 button.text.toString()으로 추출 가능
                handleKeyPress()
            }
        }

        // 제어 기능 버튼 리너 설정 (뒤로가기, 지우기)
        binding.keyBack.setOnClickListener {
            if (inputCount > 0) {
                inputCount--
                updatePinDots()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    // 4자리 충족 시 자동 바이패스 통과 로직
    private fun handleKeyPress() {
        if (inputCount < 5) {
            inputCount++
            updatePinDots()

            if (inputCount >= 4) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    // 입력 상태에 따른 상단 도트 UI 동적 렌더링
    private fun updatePinDots() {
        val dots = when (inputCount) {
            0 -> "○ ○ ○ ○ ＋ ○"
            1 -> "● ○ ○ ○ ＋ ○"
            2 -> "● ● ○ ○ ＋ ○"
            3 -> "● ● ● ○ ＋ ○"
            4 -> "● ● ● ● ＋ ○"
            5 -> "● ● ● ● ＋ ●"
            else -> "● ● ● ● ＋ ●"
        }
        binding.tvPinDots.text = dots
    }
}