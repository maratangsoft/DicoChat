package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.maratangsoft.dicochat.databinding.FragmentChattingBinding

class ChattingFragment : Fragment() {
    lateinit var binding: FragmentChattingBinding
    var chattingFragItems: MutableList<ChatItem> = mutableListOf()
    var chattingFragBsItems: MutableList<UserItem> = mutableListOf()
    var chattingFragPanelStartItems: MutableList<RoomItem> = mutableListOf()
    var chattingFragPanelEndItems: MutableList<UserItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChattingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //툴바 아이콘 리스너
        binding.panelCentral.toolbar.setNavigationOnClickListener { binding.overlappingPanels.openStartPanel() }
        binding.panelCentral.toolbar.setOnMenuItemClickListener {
            binding.overlappingPanels.openEndPanel()
            true
        }

        //바텀시트 다이얼로그 리스너
        binding.panelEnd.btnShowBs.setOnClickListener {
            val bsd = BottomSheetDialog(requireActivity())
            bsd.setContentView(layoutInflater.inflate(R.layout.fragment_chatting_bs_dialog, null))
            bsd.findViewById<RecyclerView>(R.id.recycler_chatting_bs)?.adapter =
                ChattingFragBsAdapter(requireActivity(), chattingFragBsItems)
            loadBsData()
            bsd.show()
        }

        //다른 액티비티로 이동 리스너
        binding.panelStart.btnCreateRoom.setOnClickListener {
            (requireActivity() as AppCompatActivity).startActivity(
                Intent(activity, NewRoomActivity::class.java)
            )
        }
        binding.panelEnd.btnRoomSetting.setOnClickListener {
            (requireActivity() as AppCompatActivity).startActivity(
                Intent(activity, RoomSettingActivity::class.java)
            )
        }

        //리사이클러뷰 어댑터 연결
        binding.panelCentral.recyclerPanelCentral.adapter =
            ChattingFragAdapter(requireActivity(), chattingFragItems)
        binding.panelStart.recyclerPanelStart.adapter =
            ChattingFragPanelStartAdapter(requireActivity(), chattingFragPanelStartItems)
        binding.panelEnd.recyclerPanelEnd.adapter =
            ChattingFragPanelEndAdapter(requireActivity(), chattingFragPanelEndItems)

        loadPanelCentralData()
        loadPanelStartData()
        loadPanelEndData()
    }

    private fun loadPanelCentralData(){
        chattingFragItems.add(ChatItem("1", "aaa", "2", "222222", null, null, "1", "sfde", "ff", "dd"))
        chattingFragItems.add(ChatItem("1", "aaa", "2", "222222", null, null, "1", "sfde", "ff", "dd"))
        chattingFragItems.add(ChatItem("1", "aaa", "2", "222222", null, null, "1", "sfde", "ff", "dd"))
    }

    private fun loadBsData(){
        chattingFragBsItems.add(UserItem("1", "jgdd", "null"))
        chattingFragBsItems.add(UserItem("1", "jgdd", "null"))
        chattingFragBsItems.add(UserItem("1", "jgdd", "null"))
    }

    private fun loadPanelStartData(){
        chattingFragPanelStartItems.add(RoomItem("1", "dfsfe", "null", "212312"))
        chattingFragPanelStartItems.add(RoomItem("1", "dfsfe", "null", "212312"))
        chattingFragPanelStartItems.add(RoomItem("1", "dfsfe", "null", "212312"))
    }

    private fun loadPanelEndData(){
        chattingFragPanelEndItems.add(UserItem("1", "jgdd", "null"))
        chattingFragPanelEndItems.add(UserItem("1", "jgdd", "null"))
        chattingFragPanelEndItems.add(UserItem("1", "jgdd", "null"))
    }
}