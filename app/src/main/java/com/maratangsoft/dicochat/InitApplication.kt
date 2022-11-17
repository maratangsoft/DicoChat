package com.maratangsoft.dicochat

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class InitApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "9f5e0d09109f41f54c2822919499ab20")
    }
}