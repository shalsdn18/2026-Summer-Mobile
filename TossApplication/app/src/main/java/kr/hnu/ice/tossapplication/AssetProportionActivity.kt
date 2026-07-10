package kr.hnu.ice.tossapplication

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kr.hnu.ice.tossapplication.databinding.ActivityAssetProportionBinding

class AssetProportionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssetProportionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssetProportionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        initAllocationPieChart()
    }

    // MPAndroidChart 기반 극단적 미니멀 도넛 차트 튜닝 엔진
    private fun initAllocationPieChart() {
        val chart = binding.portfolioPieChart

        // 1. 데이터 모델 정의 및 백분율 인젝션
        val entries = listOf(
            PieEntry(15.6f, "엔비디아"),
            PieEntry(12.6f, "알파벳 A"),
            PieEntry(8.2f, "삼성전자"),
            PieEntry(5.8f, "마이크론"),
            PieEntry(57.8f, "기타") // 나머지 잔여 비중 일괄 분배 가드
        )

        val dataSet = PieDataSet(entries, "Asset Proportion").apply {
            colors = listOf(
                Color.parseColor("#3182F6"), // 블루
                Color.parseColor("#00A79D"), // 틸
                Color.parseColor("#2ecc71"), // 그린
                Color.parseColor("#f1c40f"), // 옐로우
                Color.parseColor("#E5E8EB")  // 잔여 회색 처리
            )
            sliceSpace = 2f           // 데이터 조각 간의 간격 공백
            setDrawValues(false)      // 차트 내부에 수치 텍스트 강제 은닉
        }

        // 2. 차트 외형 스타일 튜닝 (토스 금융 시각화 복제)
        chart.apply {
            data = PieData(dataSet)
            isDrawHoleEnabled = true  // 도넛 구조 활성화
            setHoleColor(Color.WHITE)
            holeRadius = 78f          // 중앙 홀 크기 극대화 (반지름 두께 최소화)
            transparentCircleRadius = 0f
            description.isEnabled = false
            legend.isEnabled = false  // 하단 기본 범례 제거
            setTouchEnabled(false)    // 인터랙션 고정
            invalidate()              // 동적 렌더링 리프레시
        }
    }
}