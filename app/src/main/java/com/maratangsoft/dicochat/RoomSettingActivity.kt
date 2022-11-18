package com.maratangsoft.dicochat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.maratangsoft.dicochat.databinding.ActivityRoomSettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomSettingActivity : AppCompatActivity() {
    val binding by lazy { ActivityRoomSettingBinding.inflate(layoutInflater) }
    private val retrofitService: RetrofitService by lazy {
        RetrofitHelper.getInstance().create(RetrofitService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.etRoomTitle.setOnEditorActionListener { _, actionId, _ -> clickDone(actionId) }

        getRoomTitle()
    }

    private fun getRoomTitle(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_room_title"
        queryMap["room_no"] = ALL.currentRoomNo

        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    binding.etRoomTitle.setText(it)
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    private fun clickDone(actionId:Int): Boolean{
        if (actionId == EditorInfo.IME_ACTION_DONE){
            setRoomTitle()
        }
        return false
    }

    private fun setRoomTitle(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "set_room_title"
        queryMap["room_no"] = ALL.currentRoomNo
        queryMap["room_title"] = binding.etRoomTitle.text.toString()

        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    Toast.makeText(this@RoomSettingActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }
}