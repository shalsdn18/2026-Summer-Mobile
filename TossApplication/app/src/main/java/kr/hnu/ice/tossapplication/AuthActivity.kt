package kr.hnu.ice.tossapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
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

        // 실제 생체 인식 콜백 엔진 정의 (운영 전환 대비용)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "인증 오류: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                navigateToMain()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "인증 실패", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문으로 인증해주세요")
            .setNegativeButtonText("취소")
            .build()

        // ==========================================
        // 새 버튼 인터랙션 연결 및 테스트용 바이패스 이식
        // ==========================================

        // 1. 생체인식으로 잠금 해제 버튼 클릭 시
        binding.btnBiometricAuth.setOnClickListener {
            // [테스트 가속 가드] 실제 기기 연동을 원할 시 아래 주석을 풀고 navigateToMain()을 주석처리하십시오.
            // biometricPrompt.authenticate(promptInfo)

            Toast.makeText(applicationContext, "테스트 프리패스: 생체인식 성공", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }

        // 2. 비밀번호로 잠금 해제 버튼 클릭 시
        binding.btnPasswordAuth.setOnClickListener {
            // 즉시 통과 대신 실제 비밀번호 입력 패널 화면으로 이동
            val intent = Intent(this@AuthActivity, PasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this@AuthActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}