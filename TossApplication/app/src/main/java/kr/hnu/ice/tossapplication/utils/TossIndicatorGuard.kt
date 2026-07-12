package kr.hnu.ice.tossapplication.utils

import java.lang.IllegalArgumentException

object TossIndicatorGuard {
    // 8종 공식 심볼 마스터 카탈로그 구성
    private val VALID_INDICATORS = setOf(
        "KOSPI", "KOSDAQ", 
        "KR_BOND_2Y", "KR_BOND_3Y", "KR_BOND_5Y", 
        "KR_BOND_10Y", "KR_BOND_20Y", "KR_BOND_30Y"
    )

    /**
     * 입력된 기호 스트링이 토스증권 지표 규칙을 준수하는지 검증합니다.
     */
    fun verifySymbols(commaSeparatedSymbols: String) {
        val symbols = commaSeparatedSymbols.split(",").map { it.trim() }
        for (symbol in symbols) {
            if (!VALID_INDICATORS.contains(symbol)) {
                throw IllegalArgumentException("토스증권 제약 조건 위반: 지원하지 않는 지수/채권 심볼입니다. -> [$symbol]")
            }
        }
    }

    /**
     * 투자자별 수급 데이터 연산 집계용 심볼 제약을 검증합니다.
     */
    fun verifyInvestorTradingSymbol(symbol: String) {
        if (symbol != "KOSPI" && symbol != "KOSDAQ") {
            throw IllegalArgumentException("토스증권 제약 조건 위반: 투자자별 매매대금 지표는 오직 KOSPI 및 KOSDAQ만 지원합니다. -> [$symbol]")
        }
    }
}
