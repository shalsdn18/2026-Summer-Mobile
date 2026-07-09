package kr.hnu.ice.tossapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
// 아래 라인을 반드시 추가해야 레퍼런스 오류가 해결됩니다.
import kr.hnu.ice.tossapplication.databinding.ActivityAuthBinding
import java.util.concurrent.Executor

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)
        
        // 1. BiometricPrompt 콜백 정의
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "인증 오류: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext, "인증 성공", Toast.LENGTH_SHORT).show()
                
                // 메인 자산 대시보드 화면으로 이동
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "인증 실패: 지문을 다시 인식해주세요.", Toast.LENGTH_SHORT).show()
            }
        })

        // 2. 모달 팝업 정보 설정 (toss인증.jpg의 텍스트 매핑)
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문으로 인증해주세요")
            .setNegativeButtonText("취소")
            .build()

        // 3. 이벤트 리스너 바인딩
        binding.btnSubmitAuth.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}