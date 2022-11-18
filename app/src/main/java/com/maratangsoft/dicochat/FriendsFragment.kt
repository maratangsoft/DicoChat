package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.maratangsoft.dicochat.databinding.FragmentFriendsBinding
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsFragment : Fragment() {
    lateinit var binding: FragmentFriendsBinding
    var friendsFragItems: MutableList<UserItem> = mutableListOf()
    private val retrofitService by lazy { RetrofitHelper.getInstance().create(RetrofitService::class.java) }

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
        binding.recyclerFriends.adapter = FriendsFragmentAdapter(requireActivity(), this, friendsFragItems)
    }

    override fun onResume() {
        super.onResume()
        getFriend()
    }

    private fun getFriend(){
        friendsFragItems.clear()
        val adapter = binding.recyclerFriends.adapter
        adapter?.notifyDataSetChanged()

        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_friend"
        queryMap["user_no"] = ALL.currentUserNo

        retrofitService.getToJsonUser(queryMap).enqueue(object : Callback<MutableList<UserItem>> {
            override fun onResponse(
                call: Call<MutableList<UserItem>>,
                response: Response<MutableList<UserItem>>
            ) {
                response.body()?.let {
                    it.forEachIndexed{ i, item ->
                        friendsFragItems.add(UserItem(item.friend_no!!, null, item.nickname, item.user_img))
                        adapter?.notifyItemInserted(i)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<UserItem>>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    fun getProfileBs(position:Int){
        val bsd = BottomSheetDialog(requireActivity())
        bsd.setContentView(layoutInflater.inflate(R.layout.fragment_friends_bs, null))

        val tvNick = bsd.findViewById<AppCompatTextView>(R.id.tv_nickname)
        val tvUserNo = bsd.findViewById<AppCompatTextView>(R.id.tv_user_no)
        val civUserImg = bsd.findViewById<CircleImageView>(R.id.civ_user_img)

        bsd.show()

        tvNick?.text = friendsFragItems[position].nickname
        tvUserNo?.text = "#${friendsFragItems[position].user_no}" //getFriends()에서 friend_no를 user_no 자리에 받았으므로 여기서도 바꿔야 한다

        val imgUrl = "${ALL.BASE_URL}CicoChatServer/${friendsFragItems[position].user_img}"
        Glide.with(requireActivity()).load(imgUrl).error(R.drawable.icons8_monkey_164).into(civUserImg!!)
    }
}