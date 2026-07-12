package kr.hnu.ice.tossapplication.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * 보안이 필요한 민감 데이터(액세스 토큰 등)를 로컬에 암호화하여 저장하는 유틸리티
 */
class TossEncryptedStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "toss_encrypted_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        
        @Volatile
        private var instance: TossEncryptedStorage? = null

        fun getInstance(context: Context): TossEncryptedStorage {
            return instance ?: synchronized(this) {
                instance ?: TossEncryptedStorage(context.applicationContext).also { instance = it }
            }
        }
    }
}
