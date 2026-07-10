package kr.hnu.ice.tossapplication

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.tossapplication.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        // =================================================================
        // [추가] 자동 포커스 및 소프트 키보드 자동 팝업 제어 엔지니어링
        // =================================================================
        binding.etSearchInput.requestFocus() // 1. 검색창에 입력 초점 강제 부여
        
        // 2. 뷰가 윈도우에 안착하여 드로잉을 마친 타이밍에 키보드 올리기 (런타임 안정성 보장)
        binding.etSearchInput.post {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etSearchInput, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.etSearchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearchInput.text.toString()
                Toast.makeText(this, "'${query}' 검색 결과를 분석합니다.", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
    }
}