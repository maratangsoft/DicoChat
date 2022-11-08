package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.maratangsoft.dicochat.databinding.FragmentMentionBinding

class MentionFragment : Fragment() {
    lateinit var binding: FragmentMentionBinding
    var mentionFragItems: MutableList<ChatItem> = mutableListOf()
    var mentionFragBsItems: MutableList<RoomItem> = mutableListOf()

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
        binding.recyclerMention.adapter = MentionFragmentAdapter(requireActivity(), mentionFragItems)

        //바텀시트 다이얼로그 리스너
        binding.toolbar.setOnMenuItemClickListener {
            val bsd = BottomSheetDialog(requireActivity())
            bsd.setContentView(layoutInflater.inflate(R.layout.fragment_mention_bs_dialog, null))
            bsd.findViewById<RecyclerView>(R.id.recycler_filter)?.adapter = MentionFragmentBsAdapter(requireActivity(), mentionFragBsItems)
            loadBsData()
            bsd.show()
            true
        }

        loadData()
    }

    private fun loadData(){
        mentionFragItems.add(ChatItem(1, "aaa", null, "222222", 1, null, 1, "sfde", null))
        mentionFragItems.add(ChatItem(1, "aaa", null, "222222", 1, null, 1, "sfde", null))
        mentionFragItems.add(ChatItem(1, "aaa", null, "222222", 1, null, 1, "sfde", null))
    }

    private fun loadBsData(){
        mentionFragBsItems.clear()
        mentionFragBsItems.add(RoomItem(1, "dfsfe", null, "212312"))
        mentionFragBsItems.add(RoomItem(1, "dfsfe", null, "212312"))
        mentionFragBsItems.add(RoomItem(1, "dfsfe", null, "212312"))
    }
}