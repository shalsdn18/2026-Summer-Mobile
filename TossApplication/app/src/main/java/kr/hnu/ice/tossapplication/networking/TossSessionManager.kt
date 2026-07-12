package kr.hnu.ice.tossapplication.networking

import kr.hnu.ice.tossapplication.repository.TossStockRepository

/**
 * 사용자 세션 파괴 및 메모리 캐시 데이터를 일괄 소멸시키는 전역 관리자
 */
object TossSessionManager {

    /**
     * [Logout / Session Expired Handler]
     * 인메모리 계좌 정보 및 마스터 캐시를 클리어하여 보안 오염 및 메모리 누수를 방어합니다.
     */
    fun performLogout() {
        // 1. 계좌 세션 및 네트워크 헤더 캐시 파기
        TossAccountSession.clearAccountSession()

        // 2. 종목 마스터 데이터 및 리스크 유의사항 캐시 테이블 소멸
        TossStockRepository.clearSessionCache()
        
        // 3. 토큰 매니저 초기화 (OAuth 2.0 액세스 토큰 폐기)
        TossTokenManager.invalidateToken()
    }
}
