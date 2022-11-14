package com.maratangsoft.dicochat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.maratangsoft.dicochat.databinding.FragmentMentionBinding

class MentionFragment : Fragment() {
    lateinit var binding: FragmentMentionBinding
    var mentionFragItems: MutableList<ChatItem> = mutableListOf()

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

        getMention()
    }

    private fun getMention(){
        //TODO: 완성하기
    }
}