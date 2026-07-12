package kr.hnu.ice.tossapplication.networking

import java.net.URLEncoder
import java.io.UnsupportedEncodingException

object TossUrlEncoder {
    /**
     * ISO 8601 날짜 문자열 내부의 '+' 부호만 안전하게 %2B 로 치환하여 반환합니다.
     */
    fun encodeTimeOffset(rawDateTime: String): String {
        return try {
            if (rawDateTime.contains("+")) {
                val parts = rawDateTime.split("+")
                if (parts.size == 2) {
                    // 앞단 본체와 뒷단 타임존 오프셋 사이에 %2B 결합
                    return parts[0] + "%2B" + URLEncoder.encode(parts[1], "UTF-8")
                }
            }
            URLEncoder.encode(rawDateTime, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            rawDateTime // 실패 시 원본 풀백 가드
        }
    }
}
