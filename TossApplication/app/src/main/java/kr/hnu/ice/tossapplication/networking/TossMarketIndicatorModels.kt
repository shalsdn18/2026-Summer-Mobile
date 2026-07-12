package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 시장 지표 현재가 다건 조회 모델
data class TossIndicatorPricesResponse(
    @SerializedName("result") val result: List<IndicatorPriceItem>
)
data class IndicatorPriceItem(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("lastPrice") val lastPrice: String
)

// 2. 시장 지표 캔들 차트 모델
data class TossIndicatorCandlesResponse(
    @SerializedName("result") val result: IndicatorCandlesResult
)
data class IndicatorCandlesResult(
    @SerializedName("candles") val candles: List<IndicatorCandleItem>,
    @SerializedName("nextBefore") val nextBefore: String
)
data class IndicatorCandleItem(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("openPrice") val openPrice: String,
    @SerializedName("highPrice") val highPrice: String,
    @SerializedName("lowPrice") val lowPrice: String,
    @SerializedName("closePrice") val closePrice: String,
    @SerializedName("volume") val volume: String
)

// 3. 투자자별 매매대금 집계 모델
data class TossInvestorTradingResponse(
    @SerializedName("result") val result: InvestorTradingResult
)
data class InvestorTradingResult(
    @SerializedName("nextUntil") val nextUntil: String?,
    @SerializedName("records") val records: List<InvestorTradingRecord>
)
data class InvestorTradingRecord(
    @SerializedName("date") val date: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("individual") val individual: TradingAmount,
    @SerializedName("foreigner") val foreigner: TradingAmount,
    @SerializedName("institution") val institution: InstitutionTradingAmount,
    @SerializedName("otherCorporation") val otherCorporation: TradingAmount
)
data class TradingAmount(
    @SerializedName("buyAmount") val buyAmount: String,
    @SerializedName("sellAmount") val sellAmount: String
)
data class InstitutionTradingAmount(
    @SerializedName("buyAmount") val buyAmount: String,
    @SerializedName("sellAmount") val sellAmount: String,
    @SerializedName("breakdown") val breakdown: InstitutionBreakdown
)
data class InstitutionBreakdown(
    @SerializedName("financialInvestment") val financialInvestment: TradingAmount,
    @SerializedName("insurance") val insurance: TradingAmount,
    @SerializedName("trust") val trust: TradingAmount,
    @SerializedName("privateEquityFund") val privateEquityFund: TradingAmount,
    @SerializedName("bank") val bank: TradingAmount,
    @SerializedName("otherFinancialInstitution") val otherFinancialInstitution: TradingAmount,
    @SerializedName("pensionFund") val pensionFund: TradingAmount
)
