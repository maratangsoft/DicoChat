package com.maratangsoft.dicochat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maratangsoft.dicochat.databinding.BsInviteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InviteBSFragment(val chatFrag:ChatFragment): BottomSheetDialogFragment() {
    lateinit var binding: BsInviteBinding
    val items = mutableListOf<UserItem>()
    private val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BsInviteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFriendForInvite()
    }

    private fun getFriendForInvite(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_friend_for_invite"
        queryMap["user_no"] = ALL.currentUserNo
        queryMap["room_no"] = ALL.currentRoomNo

        retrofitService.getToJsonUser(queryMap).enqueue(object : Callback<MutableList<UserItem>> {
            override fun onResponse(
                call: Call<MutableList<UserItem>>,
                response: Response<MutableList<UserItem>>
            ) {
                response.body()?.let {
                    it.forEachIndexed{ i, item ->
                        items.add(UserItem(item.friend_no!!, null, item.nickname, item.user_img))
                        binding.recycler.adapter?.notifyItemInserted(i)
                        binding.recycler.adapter = ChatFragBsAdapter(requireActivity(), this@InviteBSFragment, items)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<UserItem>>, t: Throwable) {
                Log.d("tttGetFriend", t.message!!)
            }
        })
    }

    fun inviteUser(position:Int){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "invite_user"
        queryMap["user_no"] = items[position].user_no //getFriend()에서 friend_no를 user_no 자리에 넣었으므로 여기서도 바꿔줘야 함
        queryMap["room_no"] = ALL.currentRoomNo

        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                    chatFrag.getRoomMember()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("tttInviteUser", t.message!!)
            }
        })
    }
}