package kr.hnu.ice.tossapplication.repository

import java.util.concurrent.ConcurrentHashMap
import kr.hnu.ice.tossapplication.networking.TossNetworkClient
import kr.hnu.ice.tossapplication.networking.StockInfoItem
import kr.hnu.ice.tossapplication.networking.StockWarningItem

object TossStockRepository {
    private val apiService = TossNetworkClient.apiService

    // 스레드 안전한 인메모리 마스터 참조 캐시 테이블 구성 (메인 스레드 프리징 가드)
    private val stockInfoCache = ConcurrentHashMap<String, StockInfoItem>()
    private val stockWarningsCache = ConcurrentHashMap<String, List<StockWarningItem>>()

    /**
     * 복수 종목 정보를 캐시 우선 탐색 후 필요 시에만 원격 동기화 수신합니다.
     */
    suspend fun getStockInfoList(symbols: List<String>): List<StockInfoItem> {
        val resultList = mutableListOf<StockInfoItem>()
        val missingSymbols = mutableListOf<String>()

        // 1. 로컬 메모리 캐시 적중 상태 사전 스크리닝
        for (symbol in symbols) {
            val cached = stockInfoCache[symbol]
            if (cached != null) {
                resultList.add(cached)
            } else {
                missingSymbols.add(symbol)
            }
        }

        // 2. 캐시되지 않은 누락 종목들에 한해 상한 200건 규칙 범위 내 벌크 원격 호출 구동
        if (missingSymbols.isNotEmpty()) {
            try {
                val queryParam = missingSymbols.joinToString(",")
                val response = apiService.getStockInfo(queryParam)
                
                // 메모리 테이블 업데이트 및 최종 결과 바인딩
                response.result.forEach { item ->
                    stockInfoCache[item.symbol] = item
                    resultList.add(item)
                }
            } catch (e: Exception) {
                // 네트워크 에러 크래시 가드 리턴 처리
                e.printStackTrace()
            }
        }
        return resultList
    }

    /**
     * 종목별 매수 유의사항을 세션 내부로 캐싱 한정하여 무분별한 API 트래픽 오버헤드를 차단합니다.
     */
    suspend fun getStockWarningsWithCache(symbol: String, forceRefresh: Boolean = false): List<StockWarningItem> {
        if (!forceRefresh && stockWarningsCache.containsKey(symbol)) {
            return stockWarningsCache[symbol] ?: emptyList()
        }

        return try {
            val response = apiService.getStockWarnings(symbol)
            val warnings = response.result
            stockWarningsCache[symbol] = warnings // 빈 배열([])이어도 유효 상태로 영구 래핑
            warnings
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * 세션 종료 또는 로그아웃 시 메모리 정리를 수행하는 클리어 핸들러
     */
    fun clearSessionCache() {
        stockInfoCache.clear()
        stockWarningsCache.clear()
    }
}
