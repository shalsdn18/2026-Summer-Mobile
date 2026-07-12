package kr.hnu.ice.tossapplication.networking

import com.google.gson.annotations.SerializedName

data class TossRankingResponse(
    @SerializedName("result") val result: RankingResult
)

data class RankingResult(
    @SerializedName("rankedAt") val rankedAt: String?, // 랭킹 집계 미완료 시 null 반환 처리 대응
    @SerializedName("rankings") val rankings: List<RankingItem>
)

data class RankingItem(
    @SerializedName("rank") val rank: Int,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("currency") val currency: String, // "KRW", "USD"
    @SerializedName("price") val price: RankingPrice,
    @SerializedName("tradingVolume") val tradingVolume: String,
    @SerializedName("tradingAmount") val tradingAmount: String
)

data class RankingPrice(
    @SerializedName("lastPrice") val lastPrice: String,
    @SerializedName("basePrice") val basePrice: String, // TOP_GAINERS/LOSERS는 기점 기준가, 나머지는 전일 기준가 고정
    @SerializedName("changeRate") val changeRate: String // 기간 등락률 혹은 전일 대비 등락률 소수점 문자열
)
