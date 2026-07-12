package kr.hnu.ice.tossapplication.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kr.hnu.ice.tossapplication.networking.BusinessDayUs
import kr.hnu.ice.tossapplication.networking.UsMarketSession

object TossMarketSessionValidator {
    private val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.KOREA).apply {
        timeZone = TimeZone.getTimeZone("Asia/Seoul") // 모든 시간 KST(+09:00) 연산 고정
    }

    /**
     * 미국 시장 제공 세션 데이터를 기반으로 현재 원격 매매 거래가 가용한 상태인지 검증합니다.
     */
    fun isUsMarketOpenNow(businessDayUs: BusinessDayUs): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        
        // [Constraint Defense] 휴장일로 인해 모든 세션이 null 인 경우 즉시 폐장(false) 처리
        val sessions = listOfNotNull(
            businessDayUs.dayMarket,
            businessDayUs.preMarket,
            businessDayUs.regularMarket,
            businessDayUs.afterMarket
        )
        if (sessions.isEmpty()) return false

        for (session in sessions) {
            if (isTimeInSession(currentTimeMillis, session)) {
                return true // 단 하나의 세션이라도 현재 타임라인에 적중 시 주문 활성화
            }
        }
        return false
    }

    private fun isTimeInSession(currentTimeMillis: Long, session: UsMarketSession): Boolean {
        return try {
            val start = isoFormatter.parse(session.startTime)?.time ?: 0L
            val end = isoFormatter.parse(session.endTime)?.time ?: 0L
            currentTimeMillis in start..end
        } catch (e: Exception) {
            false
        }
    }
}
