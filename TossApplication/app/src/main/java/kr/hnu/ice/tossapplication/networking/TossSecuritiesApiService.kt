package kr.hnu.ice.tossapplication.networking

import retrofit2.Response
import retrofit2.http.*

interface TossSecuritiesApiService {

    // 1. 인증 (Auth) - OAuth 2.0 토큰 발급
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): TossTokenResponse

    // 2. 시세·종목 정보 (Market Data) - Public
    @GET("api/v1/prices")
    suspend fun getStockPrice(@Query("symbol") symbol: String): StockPriceResponse

    @GET("api/v1/candles")
    suspend fun getCandles(
        @Query("symbol") symbol: String,
        @Query("type") type: String // 1m, 1d
    ): CandleResponse

    // 3. 계좌·자산 (Account · Asset) - Private (계좌 헤더 필수)
    @GET("api/v1/accounts")
    suspend fun getAccountList(): TossAccountResponse

    @Headers("Requires-Account: true")
    @GET("api/v1/holdings")
    suspend fun getAssetHoldings(
        @Query("symbol") symbol: String? = null
    ): TossHoldingsResponse

    // =================================================================
    // [파트 4 추가] MARKET_DATA / MARKET_DATA_CHART 시세 API 라인
    // =================================================================

    /**
     * 매수/매도 호가 및 잔량 조회 (MARKET_DATA)
     */
    @GET("api/v1/orderbook")
    suspend fun getOrderbook(
        @Query("symbol") symbol: String
    ): TossOrderbookResponse

    /**
     * 종목 현재가 다건 조회 (MARKET_DATA - 최대 200건 콤마 구분)
     */
    @GET("api/v1/prices")
    suspend fun getPrices(
        @Query("symbols") symbols: String
    ): TossPricesResponse

    /**
     * 당일 최근 체결 내역 조회 (MARKET_DATA - 최대 50건)
     */
    @GET("api/v1/trades")
    suspend fun getTrades(
        @Query("symbol") symbol: String,
        @Query("count") count: Int = 50
    ): TossTradesResponse

    /**
     * 종목 당일 상한가 및 하한가 조회 (MARKET_DATA)
     */
    @GET("api/v1/price-limits")
    suspend fun getPriceLimits(
        @Query("symbol") symbol: String
    ): TossPriceLimitsResponse

    /**
     * 종목 캔들 차트 데이터 조회 (MARKET_DATA_CHART)
     * [Constraint] before 매개변수의 '+' 오프셋 부호 왜곡 방지를 위해 encoded = true 옵션 부여
     */
    @GET("api/v1/candles")
    suspend fun getCandles(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String, // "1m" 또는 "1d"
        @Query("count") count: Int = 100,
        @Query(value = "before", encoded = true) before: String? = null,
        @Query("adjusted") adjusted: Boolean = true
    ): TossCandlesResponse

    // =================================================================
    // [파트 5 추가] STOCK 종목 참조 정보 및 리스크 유의사항 API 라인
    // =================================================================

    /**
     * 종목 기본 정보 다건 조회 (STOCK - 최대 200건 지원)
     */
    @GET("api/v1/stocks")
    suspend fun getStockInfo(
        @Query("symbols") symbols: String
    ): TossStockInfoResponse

    /**
     * 종목의 매수 유의사항 및 변동성 완화(VI) 발동 실시간 정보 조회 (STOCK)
     */
    @GET("api/v1/stocks/{symbol}/warnings")
    suspend fun getStockWarnings(
        @Path("symbol") symbol: String
    ): TossStockWarningsResponse

    // =================================================================
    // [파트 6 추가] MARKET_INFO 환율 및 장 운영 일정 API 라인
    // =================================================================

    /**
     * KRW ↔ USD 표시 환율 정보 조회 (MARKET_INFO - 1분 주기 유효)
     */
    @GET("api/v1/exchange-rate")
    suspend fun getExchangeRate(
        @Query("dateTime") dateTime: String? = null,
        @Query("baseCurrency") baseCurrency: String, // USD 또는 KRW
        @Query("quoteCurrency") quoteCurrency: String
    ): TossExchangeRateResponse

    /**
     * 국내 통합 시장(KRX+NXT) 거래 가능 시간 일정 조회 (MARKET_INFO)
     */
    @GET("api/v1/market-calendar/KR")
    suspend fun getKrMarketCalendar(
        @Query("date") date: String // YYYY-MM-DD
    ): TossKrCalendarResponse

    /**
     * 미국 시장 4대 세션 장 운영 시간 일정 조회 (MARKET_INFO)
     */
    @GET("api/v1/market-calendar/US")
    suspend fun getUsMarketCalendar(
        @Query("date") date: String // YYYY-MM-DD
    ): TossUsCalendarResponse

    // =================================================================
    // [파트 7 추가] RANKING 카테고리 주식 순위 정보 API 라인
    // =================================================================

    /**
     * 지정한 시장, 기간, 기준의 주식 랭킹 다건 조회 (RANKING - 초당 최대 5회 제한)
     */
    @GET("api/v1/rankings")
    suspend fun getStockRankings(
        @Query("type") type: String, // MARKET_TRADING_AMOUNT, TOP_GAINERS 등
        @Query("marketCountry") marketCountry: String, // "KR" 또는 "US"
        @Query("duration") duration: String, // "realtime", "1d", "1w", "1mo" 등
        @Query("excludeInvestmentCaution") excludeInvestmentCaution: Boolean = false,
        @Query("count") count: Int = 20 // 최대 상한 100위
    ): TossRankingResponse

    // =================================================================
    // [파트 8 추가] MARKET_INDICATOR 카테고리 시장 지표 API 라인
    // =================================================================

    /**
     * 시장 지표(국내 지수 · 국채) 현재가 조회 (MARKET_INDICATOR - 최대 200건)
     */
    @GET("api/v1/market-indicators/prices")
    suspend fun getIndicatorPrices(
        @Query("symbols") symbols: String
    ): TossIndicatorPricesResponse

    /**
     * 시장 지표 캔들 차트 데이터 조회 (MARKET_INDICATOR_CHART)
     */
    @GET("api/v1/market-indicators/{symbol}/candles")
    suspend fun getIndicatorCandles(
        @Path("symbol") symbol: String,
        @Query("interval") interval: String, // 1m, 1d
        @Query("count") count: Int = 100,
        @Query(value = "before", encoded = true) before: String? = null
    ): TossIndicatorCandlesResponse

    /**
     * KRX 시장(코스피·코스닥)의 투자자별 매매대금 조회 (MARKET_INDICATOR)
     */
    @GET("api/v1/market-indicators/{symbol}/investor-trading")
    suspend fun getInvestorTrading(
        @Path("symbol") symbol: String, // KOSPI 또는 KOSDAQ 만 허용
        @Query("interval") interval: String, // 1d, 1w, 1mo, 1y
        @Query("count") count: Int = 20,
        @Query("until") until: String? = null
    ): TossInvestorTradingResponse

    // =================================================================
    // [파트 11 추가] ORDER 카테고리 자산 매매 트랜잭션 API 라인
    // =================================================================

    /**
     * 국내 및 미국 주식 매수/매도 주문 생성 (ORDER - 피크 타임 시 3 TPS 자동 강하 대응)
     */
    @Headers("Requires-Account: true")
    @POST("api/v1/orders")
    suspend fun createOrder(
        @Body request: TossOrderCreateRequest
    ): TossOrderResponse

    /**
     * 기존 미체결 주문의 가격 및 수량 조건 정정 (ORDER)
     */
    @Headers("Requires-Account: true")
    @POST("api/v1/orders/{orderId}/modify")
    suspend fun modifyOrder(
        @Path("orderId") orderId: String,
        @Body request: TossOrderModifyRequest
    ): TossOrderResponse

    /**
     * 미체결 정상 주문에 대한 원격 취소 처리 요청 (ORDER)
     */
    @Headers("Requires-Account: true")
    @POST("api/v1/orders/{orderId}/cancel")
    suspend fun cancelOrder(
        @Path("orderId") orderId: String,
        @Body emptyBody: Map<String, String> = emptyMap() // 빈 JSON 객체 대응 포맷
    ): TossOrderResponse

    // =================================================================
    // [파트 12 추가] ORDER_HISTORY 카테고리 자산 매매 내역 조회 API 라인
    // =================================================================

    /**
     * 상태 조건 및 기간별 주문 내역 리스트 조회 (ORDER_HISTORY - 초당 최대 5회 제한)
     */
    @Headers("Requires-Account: true")
    @GET("api/v1/orders")
    suspend fun getOrderHistoryList(
        @Query("status") status: String, // "OPEN" 또는 "CLOSED"
        @Query("symbol") symbol: String? = null,
        @Query("from") from: String? = null, // YYYY-MM-DD 기점 검색
        @Query("to") to: String? = null,
        @Query("cursor") cursor: String? = null, // OPEN 일 땐 자동 무시됨
        @Query("limit") limit: Int? = null // CLOSED 일 때 유효 (최대 100)
    ): TossOrderListResponse

    /**
     * 고유 주문 번호를 기점으로 단일 트랜잭션 상세 체결부 수집 (ORDER_HISTORY)
     */
    @Headers("Requires-Account: true")
    @GET("api/v1/orders/{orderId}")
    suspend fun getOrderDetail(
        @Path("orderId") orderId: String
    ): TossOrderDetailResponse

    // =================================================================
    // [파트 13 추가] ORDER_INFO 카테고리 거래 사전 유효 정보 API 라인
    // =================================================================

    /**
     * 매수 주문 시 사용 가능한 현금 기반 예수금 한도 조회 (ORDER_INFO)
     */
    @Headers("Requires-Account: true")
    @GET("api/v1/buying-power")
    suspend fun getBuyingPower(
        @Query("currency") currency: String // "KRW" 또는 "USD"
    ): TossBuyingPowerResponse

    /**
     * 특정 종목의 보유 청산 가능 실량 수량 조회 (ORDER_INFO)
     */
    @Headers("Requires-Account: true")
    @GET("api/v1/sellable-quantity")
    suspend fun getSellableQuantity(
        @Query("symbol") symbol: String
    ): TossSellableQuantityResponse

    /**
     * 현재 활성화된 종합매매 계좌의 시장 국가별 실효 매매 수수료율 수집 (ORDER_INFO)
     */
    @Headers("Requires-Account: true")
    @GET("api/v1/commissions")
    suspend fun getAccountCommissions(): TossCommissionsResponse

    // =================================================================
    // [파트 14 추가] CONDITIONAL_ORDER 카테고리 예약 자동 매매 API 라인
    // =================================================================

    /**
     * 특정 종목의 타겟 가격 도달 감시용 조건주문 신규 생성 (CONDITIONAL_ORDER)
     */
    @Headers("Requires-Account: true")
    @POST("api/v1/conditional-orders")
    suspend fun createConditionalOrder(
        @Body request: TossConditionalOrderCreateRequest
    ): TossConditionalOrderResponse

    /**
     * 기존 감시망 전체 재설정 및 수정 처리 (CONDITIONAL_ORDER)
     * [Notice] 수정이 수행되면 새로운 conditionalOrderId 가 발행되므로 바인딩 포인터 교체 필요
     */
    @Headers("Requires-Account: true")
    @POST("api/v1/conditional-orders/{conditionalOrderId}/modify")
    suspend fun modifyConditionalOrder(
        @Path("conditionalOrderId") conditionalOrderId: String,
        @Body request: TossConditionalOrderModifyRequest
    ): TossConditionalOrderResponse

    /**
     * 활성화 상태인 가상 조건주문 스케줄러 원격 해제 및 파기 (CONDITIONAL_ORDER)
     * [Constraint] 성공 응답이 공백(No Body)이므로 Response<Unit> 구조로 가속 랩 가드 적용
     */
    @Headers("Requires-Account: true")
    @DELETE("api/v1/conditional-orders/{conditionalOrderId}")
    suspend fun cancelConditionalOrder(
        @Path("conditionalOrderId") conditionalOrderId: String
    ): Response<Unit>

    // =================================================================
    // [파트 15 추가] CONDITIONAL_ORDER_HISTORY 카테고리 내역 조회 API 라인
    // =================================================================

    /**
     * 감시 상태 조건별 조건주문 스케줄러 목록 수집 (CONDITIONAL_ORDER_HISTORY)
     */
    @Headers("Requires-Account: true")
    @GET("api/v1/conditional-orders")
    suspend fun getConditionalOrderHistoryList(
        @Query("status") status: String, // "OPEN" 또는 "CLOSED"
        @Query("symbol") symbol: String? = null, // 종목 코드 필터 (선택)
        @Query("cursor") cursor: String? = null, // 이전 응답의 nextCursor 전달
        @Query("limit") limit: Int = 20 // 페이지 크기 한도 (최대 100)
    ): TossConditionalOrderListResponse

    /**
     * 고유 스케줄 번호를 기점으로 단일 조건주문의 세부 감시 파라미터 수집 (CONDITIONAL_ORDER_HISTORY)
     */
    @Headers("Requires-Account: true")
    @GET("api/v1/conditional-orders/{conditionalOrderId}")
    suspend fun getConditionalOrderDetail(
        @Path("conditionalOrderId") conditionalOrderId: String
    ): TossConditionalOrderDetailResponse
}
