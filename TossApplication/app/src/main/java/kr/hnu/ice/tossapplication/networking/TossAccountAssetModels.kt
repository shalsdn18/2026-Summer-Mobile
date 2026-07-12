package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// =================================================================
// 1. 계좌 목록 관련 데이터 모델
// =================================================================
data class TossAccountResponse(
    @SerializedName("result") val result: List<AccountItem>
)

data class AccountItem(
    @SerializedName("accountNo") val accountNo: String,
    @SerializedName("accountSeq") val accountSeq: Long,
    @SerializedName("accountType") val accountType: String, // 현재 BROKERAGE 고정 노출
    @SerializedName("balance") val balance: CurrencyWrapper? // 계좌 잔액 노드 추가
)

// =================================================================
// 2. 보유 주식 자산 관련 데이터 모델
// =================================================================
data class TossHoldingsResponse(
    @SerializedName("result") val result: HoldingsResult
)

data class HoldingsResult(
    @SerializedName("totalPurchaseAmount") val totalPurchaseAmount: CurrencyWrapper,
    @SerializedName("marketValue") val marketValue: MarketValueWrapper,
    @SerializedName("profitLoss") val profitLoss: ProfitLossWrapper,
    @SerializedName("dailyProfitLoss") val dailyProfitLoss: DailyProfitLossWrapper,
    @SerializedName("items") val items: List<HoldingStockItem>
)

data class CurrencyWrapper(
    @SerializedName("krw") val krw: String,
    @SerializedName("usd") val usd: String
)

data class MarketValueWrapper(
    @SerializedName("amount") val amount: CurrencyWrapper,
    @SerializedName("amountAfterCost") val amountAfterCost: CurrencyWrapper
)

data class ProfitLossWrapper(
    @SerializedName("amount") val amount: CurrencyWrapper,
    @SerializedName("amountAfterCost") val amountAfterCost: CurrencyWrapper,
    @SerializedName("rate") val rate: String,
    @SerializedName("rateAfterCost") val rateAfterCost: String
)

data class DailyProfitLossWrapper(
    @SerializedName("amount") val amount: CurrencyWrapper,
    @SerializedName("rate") val rate: String
)

data class HoldingStockItem(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("marketCountry") val marketCountry: String, // "KR" 또는 "US"
    @SerializedName("currency") val currency: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("lastPrice") val lastPrice: String,
    @SerializedName("averagePurchasePrice") val averagePurchasePrice: String,
    @SerializedName("logoUrl") val logoUrl: String?, // 로고 이미지 URL (추가)
    @SerializedName("marketValue") val itemMarketValue: ItemMarketValue,
    @SerializedName("profitLoss") val itemProfitLoss: ItemProfitLoss,
    @SerializedName("dailyProfitLoss") val itemDailyProfitLoss: ItemDailyProfitLoss,
    @SerializedName("cost") val cost: CostDetails
)

data class ItemMarketValue(
    @SerializedName("purchaseAmount") val purchaseAmount: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("amountAfterCost") val amountAfterCost: String
)

data class ItemProfitLoss(
    @SerializedName("amount") val amount: String,
    @SerializedName("amountAfterCost") val amountAfterCost: String,
    @SerializedName("rate") val rate: String,
    @SerializedName("rateAfterCost") val rateAfterCost: String
)

data class ItemDailyProfitLoss(
    @SerializedName("amount") val amount: String,
    @SerializedName("rate") val rate: String
)

data class CostDetails(
    @SerializedName("commission") val commission: String,
    @SerializedName("tax") val tax: String
)
