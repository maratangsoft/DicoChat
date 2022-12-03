package com.maratangsoft.dicochat

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.maratangsoft.dicochat.databinding.FragmentMentionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MentionFragment : Fragment() {
    lateinit var binding: FragmentMentionBinding
    var items: MutableList<ChatItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMentionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //리사이클러뷰 어댑터
        binding.recyclerMention.adapter = MentionFragAdapter(requireActivity(), items)

        getMention()
    }

    private fun getMention(){
        items.clear()
        val adapter = binding.recyclerMention.adapter
        adapter?.notifyItemRangeRemoved(0, adapter.itemCount - 1)

        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_mention"
        queryMap["user_no"] = ALL.currentUserNo

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        retrofitService.getToJsonChat(queryMap).enqueue(object : Callback<MutableList<ChatItem>> {
            override fun onResponse(
                call: Call<MutableList<ChatItem>>,
                response: Response<MutableList<ChatItem>>
            ) {
                response.body()?.let {
                    it.forEachIndexed{ i, item ->
                        items.add(item)
                        adapter?.notifyItemInserted(i)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<ChatItem>>, t: Throwable) {
                Log.d("tttGetMention", t.message!!)
            }
        })
    }
}