package kr.hnu.ice.tossapplication.utils

import kr.hnu.ice.tossapplication.networking.TossConditionRequest

object TossConditionalOrderGuard {

    /**
     * 조건주문 타입별 비즈니스 제약 조건을 검증합니다.
     * @throws IllegalArgumentException 검증 실패 시 상세 사유와 함께 예외 발생
     */
    fun validateConditionalOrder(
        type: String,
        orderType: String,
        first: TossConditionRequest,
        second: TossConditionRequest?
    ) {
        // 1. 공통: LIMIT 인 경우 orderPrice 필수 체크
        if (orderType == "LIMIT") {
            if (first.orderPrice == null) throw IllegalArgumentException("첫 번째 조건의 지정가가 누락되었습니다.")
            if (type != "SINGLE" && second?.orderPrice == null) throw IllegalArgumentException("두 번째 조건의 지정가가 누락되었습니다.")
        }

        // 2. 타입별 상세 검증
        when (type) {
            "SINGLE" -> {
                // SINGLE은 특별한 추가 제약 없음
            }
            "OCO" -> {
                if (second == null) throw IllegalArgumentException("OCO 타입은 두 번째 조건이 필수입니다.")
                if (orderType != "LIMIT") throw IllegalArgumentException("OCO 타입은 지정가(LIMIT) 주문만 허용됩니다.")
                if (first.orderSide != "SELL" || second.orderSide != "SELL") {
                    throw IllegalArgumentException("OCO 타입은 양방향 모두 매도(SELL)여야 합니다.")
                }
                
                val fTrigger = first.triggerPrice.toDoubleOrNull() ?: 0.0
                val sTrigger = second.triggerPrice.toDoubleOrNull() ?: 0.0
                if (fTrigger <= sTrigger) {
                    throw IllegalArgumentException("OCO 익절가(First)는 손절가(Second)보다 커야 합니다.")
                }
            }
            "OTO" -> {
                if (second == null) throw IllegalArgumentException("OTO 타입은 두 번째 조건이 필수입니다.")
                if (orderType != "LIMIT") throw IllegalArgumentException("OTO 타입은 지정가(LIMIT) 주문만 허용됩니다.")
                if (first.orderSide != "BUY" || second.orderSide != "SELL") {
                    throw IllegalArgumentException("OTO 타입은 매수(BUY) 후 매도(SELL) 구성이어야 합니다.")
                }
            }
            else -> throw IllegalArgumentException("지원하지 않는 조건주문 타입입니다: $type")
        }
    }
}
