package com.maratangsoft.dicochat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maratangsoft.dicochat.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGoogleLogin.setOnClickListener { googleLogin() }
        binding.btnKakaoLogin.setOnClickListener { kakaoLogin() }
        binding.btnGuestLogin.setOnClickListener { guestLogin() }

        getUserNo()
    }

    private fun googleLogin(){
        //TODO: complete
    }

    private fun kakaoLogin(){
        //TODO: complete
    }

    private fun guestLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getUserNo(){
        //TODO: complete
    }
}