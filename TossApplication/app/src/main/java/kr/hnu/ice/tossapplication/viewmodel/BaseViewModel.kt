package kr.hnu.ice.tossapplication.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.hnu.ice.tossapplication.networking.TossErrorHandler
import kr.hnu.ice.tossapplication.networking.TossTokenManager

/**
 * 전역 비동기 예외 가드 및 공통 상태를 관리하는 베이스 뷰모델 레이어
 */
abstract class BaseViewModel : ViewModel() {

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    /**
     * [Critical Session Guard] 모든 API 호출 전 유효한 OAuth 토큰이 확보되었는지 보장합니다.
     */
    protected suspend fun ensureAuthenticated() {
        TossTokenManager.getValidToken()
    }

    /**
     * [Global Exception Guard] 
     * viewModelScope.launch 구동 중 발생하는 예외를 전역 포획하여 TossErrorHandler로 정규화 파싱합니다.
     */
    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val errorEnvelope = TossErrorHandler.parseError(throwable)
        val errorMessage = errorEnvelope?.error?.message ?: "알 수 없는 오류가 발생했습니다."
        _errorState.value = errorMessage
        handleException(throwable, errorMessage)
    }

    /**
     * 자식 클래스에서 추가적인 예외 처리가 필요할 경우 오버라이드 가능
     */
    protected open fun handleException(throwable: Throwable, message: String) {
        // 기본 로그 출력 또는 추가 공통 처리
    }

    fun clearError() {
        _errorState.value = null
    }
}
