package kr.hnu.ice.tossapplication.repository

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.networking.CommissionItem

object TossPreOrderChecker {
    private val apiService = TossNetworkClient.apiService
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    /**
     * 매수 진입 전, 사용자의 가용 현금 바인딩 상태를 정밀 연산 및 스크리닝 가드 처리합니다.
     * @return 매수 가능 여부 (가용 금액 초과 시 false 반환 후 단절)
     */
    suspend fun verifyAssetBuyingPower(currency: String, totalOrderAmount: BigDecimal): Boolean {
        return try {
            val response = apiService.getBuyingPower(currency)
            val cashPower = BigDecimal(response.result.cashBuyingPower)
            
            // 주문 요청 총액이 가용 현금 풀 범위 내에 들어오는지 산술 비교
            totalOrderAmount <= cashPower
        } catch (e: Exception) {
            false // 통신 장애 시 안전을 위해 매매 접수 전단 차단 가드(false) 폴백
        }
    }

    /**
     * 매도 진입 전, 잔고 증명 락 단계를 거쳐 오버 매도 가능성을 전단 분리합니다.
     */
    suspend fun verifyAssetSellableQuantity(symbol: String, orderQuantity: BigDecimal): Boolean {
        return try {
            val response = apiService.getSellableQuantity(symbol)
            val sellableQty = BigDecimal(response.result.sellableQuantity)
            
            orderQuantity <= sellableQty
        } catch (e: Exception) {
            false
        }
    }

    /**
     * [Constraint Resolution] 수수료 배열 응답 노드 중, 2026년 현재 비즈니스 윈도에 적합한 수수료 지표를 추출합니다.
     */
    suspend fun getEffectiveCommissionRate(marketCountry: String): BigDecimal {
        try {
            val response = apiService.getAccountCommissions()
            val today = Date() // 2026년 기준 실시간 시스템 데이트 동기화

            val matchedRate = response.result.firstOrNull { item ->
                item.marketCountry == marketCountry && isWithinTimeline(today, item.startDate, item.endDate)
            }

            return if (matchedRate != null) {
                BigDecimal(matchedRate.commissionRate)
            } else {
                BigDecimal("0.0015") // 매칭 지표 누락 시 표준 토스 기본 수수료율 풀백 할당
            }
        } catch (e: Exception) {
            return BigDecimal("0.0015")
        }
    }

    private fun isWithinTimeline(today: Date, startStr: String?, endStr: String?): Boolean {
        return try {
            val start = startStr?.let { dateFormatter.parse(it) }
            val end = endStr?.let { dateFormatter.parse(it) }

            val afterStart = start?.let { today.after(it) || today == it } ?: true
            val beforeEnd = end?.let { today.before(it) || today == it } ?: true

            afterStart && beforeEnd
        } catch (e: Exception) {
            true
        }
    }
}
