package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

// 1. 환율 조회 데이터 모델
data class TossExchangeRateResponse(
    @SerializedName("result") val result: ExchangeRateResult
)

data class ExchangeRateResult(
    @SerializedName("baseCurrency") val baseCurrency: String, // "USD", "KRW"
    @SerializedName("quoteCurrency") val quoteCurrency: String,
    @SerializedName("rate") val rate: String,
    @SerializedName("midRate") val midRate: String,
    @SerializedName("basisPoint") val basisPoint: String,
    @SerializedName("rateChangeType") val rateChangeType: String, // "UP", "DOWN", "SAME"
    @SerializedName("validFrom") val validFrom: String,
    @SerializedName("validUntil") val validUntil: String
)

// 2. 국내 장 운영 정보 데이터 모델 (KRX + NXT 통합)
data class TossKrCalendarResponse(
    @SerializedName("result") val result: KrCalendarResult
)

data class KrCalendarResult(
    @SerializedName("today") val today: BusinessDayKr,
    @SerializedName("previousBusinessDay") val previousBusinessDay: BusinessDayKr,
    @SerializedName("nextBusinessDay") val nextBusinessDay: BusinessDayKr
)

data class BusinessDayKr(
    @SerializedName("date") val date: String, // YYYY-MM-DD
    @SerializedName("integrated") val integrated: IntegratedSessions
)

data class IntegratedSessions(
    @SerializedName("preMarket") val preMarket: KrMarketSession,
    @SerializedName("regularMarket") val regularMarket: KrMarketSession,
    @SerializedName("afterMarket") val afterMarket: KrMarketSession
)

data class KrMarketSession(
    @SerializedName("startTime") val startTime: String,
    @SerializedName("singlePriceAuctionStartTime") val singlePriceAuctionStartTime: String?, // 국내 단일가 시작 시각
    @SerializedName("singlePriceAuctionEndTime") val singlePriceAuctionEndTime: String?,
    @SerializedName("endTime") val endTime: String
)

// 3. 해외 장 운영 정보 데이터 모델 (US 4대 세션 결합형)
data class TossUsCalendarResponse(
    @SerializedName("result") val result: UsCalendarResult
)

data class UsCalendarResult(
    @SerializedName("today") val today: BusinessDayUs,
    @SerializedName("previousBusinessDay") val previousBusinessDay: BusinessDayUs,
    @SerializedName("nextBusinessDay") val nextBusinessDay: BusinessDayUs
)

data class BusinessDayUs(
    @SerializedName("date") val date: String,
    // [Constraint] 미국 휴장 시 하위 4대 세션 구조체가 통째로 null 하강 반환됨을 방어
    @SerializedName("dayMarket") val dayMarket: UsMarketSession?,
    @SerializedName("preMarket") val preMarket: UsMarketSession?,
    @SerializedName("regularMarket") val regularMarket: UsMarketSession?,
    @SerializedName("afterMarket") val afterMarket: UsMarketSession?
)

data class UsMarketSession(
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String
)
