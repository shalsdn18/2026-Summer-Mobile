package kr.hnu.ice.tossapplication.utils

import java.lang.IllegalArgumentException
import java.math.BigDecimal

object TossOrderValidationGuard {

    /**
     * 주문 생성 전 토스증권 비즈니스 룰 위반 여부를 선제적으로 스크리닝합니다.
     */
    fun validateCreateOrder(
        isUsStock: Boolean,
        side: String,
        orderType: String,
        quantity: String,
        price: String?,
        confirmHighValueOrder: Boolean
    ) {
        // 1. LIMIT 주문 시 가격 누락 방어
        if (orderType == "LIMIT" && price.isNullOrBlank()) {
            throw IllegalArgumentException("지정가(LIMIT) 주문에는 수치화된 가격 필드가 필수적입니다.")
        }

        // 2. 소수점 수량 조건 가드 검증
        if (quantity.contains(".")) {
            val isUsFractionalSell = isUsStock && orderType == "MARKET" && side == "SELL"
            if (!isUsFractionalSell) {
                throw IllegalArgumentException("소수점 수량은 미국 주식 시장가 매도 주문 규칙 하에서만 허용됩니다.")
            }
            
            val decimalScale = BigDecimal(quantity).scale()
            if (decimalScale > 6) {
                throw IllegalArgumentException("소수점 수량은 최대 6자리 까지만 연산 범위로 인정됩니다.")
            }
        }

        // 3. 1억원 이상 고액 착오 주문 플래그 체크 (가상 원화 산정 기준 안전 가드)
        if (!price.isNullOrBlank() && !quantity.contains(".")) {
            val estimatedAmount = BigDecimal(price).multiply(BigDecimal(quantity))
            if (!isUsStock && estimatedAmount >= BigDecimal("100000000") && !confirmHighValueOrder) {
                throw IllegalArgumentException("1억원 이상의 국내 매매 주문에는 confirmHighValueOrder 승인 정보가 동반되어야 합니다.")
            }
        }
    }

    /**
     * 주문 정정 처리 시 국가별 파라미터 교차 오염 현상을 차단합니다.
     */
    fun validateModifyOrder(isUsStock: Boolean, quantity: String?) {
        // [Constraint Case] 미국 주식 정정인데 수량 필드가 유입된 경우 전단 차단
        if (isUsStock && !quantity.isNullOrBlank()) {
            throw IllegalArgumentException("미국 주식은 수량 정정을 지원하지 않으므로 가격 필드 변경만 시도하십시오.")
        }
        
        // 국내 주식인데 수량이 누락된 경우 방어
        if (!isUsStock && quantity.isNullOrBlank()) {
            throw IllegalArgumentException("국내 주식 정정 요청에는 수량(quantity) 매개변수가 필수적으로 할당되어야 합니다.")
        }
    }
}
