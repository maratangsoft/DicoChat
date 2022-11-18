package com.maratangsoft.dicochat

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFCMService: FirebaseMessagingService() {
    //신규 토큰 발급받았을 때 자동 발동하는 콜백
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("CICO-FCM-Token", token)

        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "register_FCM_token"
        queryMap["fcm_token"] = token

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let {
                    if (it == "fail")
                        Log.d("CICO-FCM-Token", R.string.msg_empty_response.toString())
                    else
                        Log.d("CICO-FCM-Token", "FCM토큰 저장 성공")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICO-FCM-error", t.message!!)
            }
        })
    }

    //푸시 받았을 때 자동 발동하는 콜백
    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        Log.i("CICO-FCM-Received", "onMessage Received...")

        val data = msg.data
        if (data.isEmpty()) return

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("chat_no", data["chat_no"])
        intent.putExtra("room_no", data["room_no"])
        intent.putExtra("user_no", data["user_no"])
        intent.putExtra("message", data["message"])
        intent.putExtra("fileUrl", data["fileUrl"])
        intent.putExtra("mentioned", data["mentioned"])
        intent.putExtra("writeDate", data["writeDate"])
        intent.putExtra("nickname", data["nickname"])
        intent.putExtra("userImg", data["userImg"])

        startActivity(intent)

        //사용자에게는 메시지 수신을 Notification으로 통지해야 함
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
        builder.setContentText("${data["nickname"]}\n${data["message"]}")

        manager.notify(20, builder.build()) //id는 아무거나 쓰면 됨
    }
}