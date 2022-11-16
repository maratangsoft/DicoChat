package com.maratangsoft.dicochat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.maratangsoft.dicochat.databinding.ActivityNewRoomBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class NewRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityNewRoomBinding.inflate(layoutInflater) }
    private var imgPath = ""

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode != Activity.RESULT_CANCELED){
            val uri = result.data?.data
            Glide.with(this@NewRoomActivity).load(uri).into(binding.civRoomImg)

            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val loader = CursorLoader(this, uri!!, proj, null, null, null)
            val cursor = loader.loadInBackground()
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            imgPath = cursor.getString(columnIndex)
            cursor.close()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnRoomImg.setOnClickListener { setRoomImg() }
        binding.btnRegisterRoom.setOnClickListener { registerRoom() }
    }

    private fun setRoomImg() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private fun registerRoom() {
        if (binding.etRoomTitle.text == null || imgPath == "") {
            Toast.makeText(this, R.string.error_actNR_no_input, Toast.LENGTH_SHORT).show()
            return
        }

        val dataPart = mutableMapOf<String, String>()
        dataPart["type"] = "register_room"
        dataPart["room_title"] = binding.etRoomTitle.text.toString()
        dataPart["user_no"] = ALL.currentUserNo

        val file = File(imgPath)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val filePart = MultipartBody.Part.createFormData("img", file.name, requestBody)

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        retrofitService.postToPlain(dataPart, filePart).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    if (it == "fail")
                        Toast.makeText(this@NewRoomActivity, R.string.error_empty_response, Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this@NewRoomActivity, R.string.msg_actNR_registered, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }
}