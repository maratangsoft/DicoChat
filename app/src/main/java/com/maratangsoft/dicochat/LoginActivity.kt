package com.maratangsoft.dicochat

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.maratangsoft.dicochat.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private var googleId:String = ""
    private var kakaoId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGoogleLogin.setOnClickListener { authenticateUser(it) }
        binding.btnKakaoLogin.setOnClickListener { authenticateUser(it) }
        binding.btnGuestLogin.setOnClickListener { authenticateUser(it) }
    }

    private fun authenticateUser(view:View){
        when (view){
            binding.btnGoogleLogin -> {
                //TODO: Google Login API
                googleId = "10"
            }
            binding.btnKakaoLogin -> {
                //TODO: Kakao Login API
                kakaoId = "230"
            }
        }
        getUserNo()
    }

    private fun getUserNo(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_user_no"
        queryMap["google_id"] = googleId
        queryMap["kakao_id"] = kakaoId
        Log.d("CICOCHAT-querymap", queryMap.toString())

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d("CICOCHAT", response.body().toString())
                response.body()?.let {
                    ALL.currentUserNo = it
                    Toast.makeText(this@LoginActivity, "#${ALL.currentUserNo}님, 환영합니다.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }
}