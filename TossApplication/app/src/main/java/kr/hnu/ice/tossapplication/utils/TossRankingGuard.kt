package kr.hnu.ice.tossapplication.utils

import java.lang.IllegalArgumentException

object TossRankingGuard {
    /**
     * 토스증권 Open API 규격을 위반하는 매개변수 조합인지 사전 스크리닝을 수행합니다.
     * @return 위반 상태인 경우 true, 정상 호출 가능 조합인 경우 false 리턴
     */
    fun isInvalidCombination(type: String, duration: String): Boolean {
        val isGainersOrLosers = type == "TOP_GAINERS" || type == "TOP_LOSERS"
        val isRealtime = duration == "realtime"
        
        // [Constraint Case] 등락률 정렬 지표이면서 실시간 윈도우 파라미터가 들어온 경우 차단
        return isGainersOrLosers && isRealtime
    }

    /**
     * 안전한 원격 데이터 게이트웨이 호출을 보장하기 위한 무결성 검증 래퍼 함수
     */
    fun validateRankingParams(type: String, duration: String) {
        if (isInvalidCombination(type, duration)) {
            throw IllegalArgumentException(
                "토스증권 Open API 제약 조건 위반: 급상승(TOP_GAINERS) 및 급하락(TOP_LOSERS) 지표는 실시간(realtime) 기간 조회를 지원하지 않습니다."
            )
        }
    }
}
