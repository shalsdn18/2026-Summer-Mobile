package kr.hnu.ice.pickerdiolog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.pickerdiolog.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DatePickerDialog 구현부
        binding.dateBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                binding.dateText.text = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth)
            }, y, m, d).show()
        }

        // TimePickerDialog 구현부
        binding.timeBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, hourOfDay, minuteOfHour ->
                binding.timeText.text = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour)
            }, hour, minute, true).show()
        }

        // 1. 완전히 깨져있던 dlgBtn1 오작동 코드 정상화
        binding.dlgBtn1.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("dialog1")
                setMessage("첫 번째 기본 대화상자입니다.")
                setPositiveButton("확인", null)
                show()
            }
        }

        // 2. image_bf00ea.jpg 스크린샷 내 알림 대화상자(확인/취소) 로직 복구 부
        binding.alertBtn.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("알림")
                setMessage("이것은 알림 대화상자입니다.")
                setPositiveButton("확인") { dialog, _ ->
                    Toast.makeText(this@MainActivity, "확인을 눌렀습니다.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }
    }
}