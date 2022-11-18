package com.maratangsoft.dicochat

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMService: FirebaseMessagingService() {
    //앱 처음 시작시 신규 토큰 발급받음. 이때 자동 발동하는 콜백
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("CICO-FCM-Token", token)
        ALL.currentUserFCMToken = token
    }

    //푸시 받았을 때 자동 발동하는 콜백
    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        Log.i("CICO-FCM-Received", "onMessage Received...")

        //받은 데이터 메인액티비티로 보내기
        val bundle = bundleOf("fcm_push" to msg.data)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
        Log.d("CICO-FCM-data", msg.data.toString())

        //사용자에게는 메시지 수신을 Notification으로 통지해야 함
        //TODO: 현재 푸시가 디바이스 단위로 오는 상황임. 같은 기기로 여러 계정 사용시 푸시 구분이 안됨.
        val manager = NotificationManagerCompat.from(this)
        val builder = if (Build.VERSION.SDK_INT < 26){
            NotificationCompat.Builder(this, "")
        }else{
            val channel = NotificationChannelCompat
                .Builder("fcm_ch", NotificationManagerCompat.IMPORTANCE_HIGH)
                .setName(R.string.app_name.toString())
                .build()
            manager.createNotificationChannel(channel)
            NotificationCompat.Builder(this, "fcm_ch")
        }

        val notiIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            100,
            notiIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)
        builder.setContentTitle(R.string.noti_title.toString())
        builder.setContentText("${msg.data["nickname"]}\n${msg.data["message"]}")

        manager.notify(20, builder.build()) //id는 아무거나 쓰면 됨
    }
}