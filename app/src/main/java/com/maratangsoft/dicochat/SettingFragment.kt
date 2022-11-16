package com.maratangsoft.dicochat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.maratangsoft.dicochat.databinding.FragmentSettingBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
    private val retrofitService by lazy { RetrofitHelper.getInstance().create(RetrofitService::class.java) }
    private var imgPath = ""

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode != Activity.RESULT_CANCELED){
            val uri = result.data?.data
            Glide.with(requireActivity()).load(uri).into(binding.civUserImg)

            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val loader = CursorLoader(requireActivity(), uri!!, proj, null, null, null)
            val cursor = loader.loadInBackground()
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            imgPath = cursor.getString(columnIndex)
            cursor.close()

            Log.d("CICO-FragS-imgPath", imgPath)
            setUserImg()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUserNo.text = "#${ALL.currentUserNo}"
        binding.civUserImg.setOnClickListener { goToGallery() }
        binding.tvNickname.setOnClickListener { openNickDialog() }
        binding.btnLogout.setOnClickListener { logout() }

        getProfile()
    }

    private fun getProfile(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_profile"
        queryMap["user_no"] = ALL.currentUserNo

        retrofitService.getToJsonUser(queryMap).enqueue(object : Callback<MutableList<UserItem>> {
            override fun onResponse(
                call: Call<MutableList<UserItem>>,
                response: Response<MutableList<UserItem>>
            ) {
                response.body()?.let {
                    Log.d("CICO-FragSet-response", it.toString())
                    binding.tvNickname.text = it[0].nickname
                    Glide.with(requireActivity()).load("${ALL.BASE_URL}CicoChatServer/${it[0].user_img}").error(R.drawable.icons8_monkey_164).into(binding.civUserImg)
                }
            }
            override fun onFailure(call: Call<MutableList<UserItem>>, t: Throwable) {
                Log.d("CICO-FragS-getProfile", t.message!!)
            }
        })
    }

    private fun openNickDialog(){
        val dialog = AlertDialog.Builder(requireActivity()).setView(R.layout.fragment_setting_dialog_change_nick).show()

        val et = dialog.findViewById<AppCompatEditText>(R.id.et_change_nickname)
        val btnOk = dialog.findViewById<AppCompatTextView>(R.id.btn_ok)
        val btnCancel = dialog.findViewById<AppCompatTextView>(R.id.btn_cancel)

        btnOk?.setOnClickListener {
            binding.tvNickname.text = et?.text
            setNickname()
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener { dialog.dismiss() }
    }

    private fun setNickname(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "set_nickname"
        queryMap["user_no"] = ALL.currentUserNo
        queryMap["nickname"] = binding.tvNickname.text.toString()

        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    if (it == "fail")
                        Toast.makeText(requireActivity(), R.string.error_empty_response, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICO-FragS-setNickname", t.message!!)
            }
        })
    }

    private fun goToGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private fun setUserImg(){
        val dataPart = mutableMapOf<String, String>()
        dataPart["type"] = "set_user_img"
        dataPart["room_no"] = ALL.currentRoomNo
        dataPart["user_no"] = ALL.currentUserNo

        val file = File(imgPath)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val filePart = MultipartBody.Part.createFormData("img", file.name, requestBody)

        retrofitService.postToPlain(dataPart, filePart).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    if (it == "fail")
                        Log.d("CICO-sdfsdf", it)
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICO-FragSet-setUserImg", t.message!!)
            }
        })
    }

    private fun logout(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(R.string.tv_fragS_dialogLO_question)
        builder.setPositiveButton(R.string.btn_yes) { _, _ ->
            //TODO: 자동로그인 처리 해제
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
        builder.setNegativeButton(R.string.btn_no, null)
        builder.show()
    }
}