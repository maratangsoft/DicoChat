package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.maratangsoft.dicochat.databinding.FragmentFriendsBinding

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
        //TODO: 완성하기
    }
}