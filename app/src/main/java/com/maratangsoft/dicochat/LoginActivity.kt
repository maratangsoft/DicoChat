package com.maratangsoft.dicochat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.maratangsoft.dicochat.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private var googleId = ""
    private var kakaoId = ""
    private var fcmToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnGoogleLogin.setSize(SignInButton.SIZE_WIDE)
        getFCMToken()
        binding.btnGoogleLogin.setOnClickListener { authenticateUser(it) }
        binding.btnKakaoLogin.setOnClickListener { authenticateUser(it) }
        binding.btnGuestLogin.setOnClickListener { authenticateUser(it) }
    }

    private fun getFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful){
                Log.i("CICO-ActL-getFCMToken", "FCM 등록 실패")
            }else{
                fcmToken = it.result
                Log.i("CICO-ActL-getFCMToken", fcmToken)
                ALL.currentUserFCMToken = fcmToken
            }
        }
    }

    private fun authenticateUser(view:View){
        when (view){
            binding.btnGoogleLogin -> loginWithGoogle()
            binding.btnKakaoLogin -> loginWithKakao()
            binding.btnGuestLogin -> getUserNo()
        }
    }
//////////////////////////Google Sign In/////////////////////////////////////

    private fun loginWithGoogle(){
        //기존 로그인 이력이 있다면 자동로그인
        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?.let {
            googleId = account.id.toString()
            getUserNo()
        }

        //클라이언트ID로 펜딩인텐트 요청 만들기
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //런처에 실어보낼 펜딩인텐트 얻어오기
        val signInIntent = mGoogleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    //로그인 성공시 토큰 받아서 유저정보 추출하는 콜백 설계
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d("CICO-ActL-ActivityResultCallback", it.resultCode.toString())
            if (it.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d("CICO-ActL-ActivityResultCallback", it.toString())

                    ALL.currentUserNo = account.id!!
                    getUserNo()

                }catch (e: ApiException) {
                    Log.e("CICO-ActL-ActivityResultCallback", e.message.toString())
                }
            }
    }
//////////////////////////Kakao Login/////////////////////////////////////

    private fun loginWithKakao(){
        //카톡앱 로그인 가능하면 카톡앱 로그인, 아니면 카카오계정 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callbackKakao)
        }else{
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callbackKakao)
        }
    }
    //로그인 성공시 토큰 받아오는 콜백 설계
    private val callbackKakao: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        token?.let {
            loadKakaoUserinfo()
        }
    }
    //토큰에서 유저 정보 추출하는 콜백 설계
    private fun loadKakaoUserinfo(){
        UserApiClient.instance.me { user, error ->
            if (user != null){
                kakaoId = user.id.toString()
                getUserNo()

            }else{
                Log.d("CICO-ActL-loadKakaoUserInfo", error?.message.toString())
            }
        }
    }
/////////////////////////공통////////////////////////////////////////////////

    private fun getUserNo(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_user_no"
        queryMap["google_id"] = googleId
        queryMap["kakao_id"] = kakaoId
        queryMap["fcm_token"] = ALL.currentUserFCMToken

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
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