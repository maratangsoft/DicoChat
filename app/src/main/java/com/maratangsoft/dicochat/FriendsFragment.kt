package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.maratangsoft.dicochat.databinding.FragmentFriendsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsFragment : Fragment() {
    lateinit var binding: FragmentFriendsBinding
    var friendsFragItems: MutableList<UserItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener {
            (requireActivity() as AppCompatActivity).startActivity(Intent(activity, FindFriendActivity::class.java))
            true
        }
        binding.recyclerFriends.adapter = ChattingFragPanelEndAdapter(requireActivity(), friendsFragItems)

        getFriend()
    }

    private fun getFriend(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_friend"
        queryMap["user_no"] = ALL.currentUserNo

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
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
}