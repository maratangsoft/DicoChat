package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.maratangsoft.dicochat.databinding.FragmentSettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
    private val retrofitService: RetrofitService by lazy {
        RetrofitHelper.getInstance().create(RetrofitService::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.civUserImg.setOnClickListener { setUserImg() }
        binding.tvNickname.setOnClickListener { setNickname() }
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
                    val result = it

                }
            }

            override fun onFailure(call: Call<MutableList<UserItem>>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
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
                    val result = it

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    private fun setUserImg(){
        //TODO: complete
    }

    private fun logout(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(R.string.tv_fragS_dialogLO_question)
        builder.setPositiveButton(R.string.btn_yes) { _, _ ->
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
        builder.setNegativeButton(R.string.btn_no, null)
        builder.show()
    }
}