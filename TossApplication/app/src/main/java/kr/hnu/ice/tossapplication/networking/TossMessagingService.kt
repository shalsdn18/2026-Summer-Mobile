package kr.hnu.ice.tossapplication.networking

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.hnu.ice.tossapplication.MainActivity
import kr.hnu.ice.tossapplication.R

/**
 * 실시간 체결 알림 및 백그라운드 푸시 메시지를 처리하는 FCM 서비스
 */
class TossMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // 데이터 페이로드가 포함된 경우 체결 정보 파싱 가동
        remoteMessage.data.isNotEmpty().let {
            val title = remoteMessage.data["title"] ?: "토스증권 알림"
            val body = remoteMessage.data["body"] ?: "새로운 소식이 도착했습니다."
            sendNotification(title, body)
        }
    }

    override fun onNewToken(token: String) {
        // 새로운 FCM 토큰이 발행될 경우 서버에 동기화 요청 (가상)
        android.util.Log.d("FCM_CORE", "New FCM Token: $token")
    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = android.app.PendingIntent.getActivity(
            this, 0, intent,
            android.app.PendingIntent.FLAG_ONE_SHOT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "toss_trade_alert"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android 8.0 이상 알림 채널 생성 가드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "실시간 체결 알림",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
