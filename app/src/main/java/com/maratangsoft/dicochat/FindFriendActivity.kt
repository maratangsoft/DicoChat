package com.maratangsoft.dicochat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.maratangsoft.dicochat.databinding.ActivityFindFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindFriendActivity : AppCompatActivity() {
    val binding by lazy { ActivityFindFriendBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnRegisterFriend.setOnClickListener { registerFriend() }
    }

    private fun registerFriend(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "register_friend"
        queryMap["user_no"] = ALL.currentUserNo
        queryMap["friend_no"] = binding.etUserNo.text.toString()

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    val result = it

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }
}