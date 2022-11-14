package com.maratangsoft.dicochat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
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

//////////////가운데 패널 뷰 설정////////////////
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
            getFriend()
            bsd.show()
        }
        //채팅목록 리사이클러뷰
        binding.panelCentral.recyclerPanelCentral.adapter =
            ChattingFragAdapter(requireActivity(), chattingFragItems)

        getChat()

        //보내기 버튼 리스너
        binding.panelCentral.btnSendChat.setOnClickListener { sendChat() }
        binding.panelCentral.btnSendFile.setOnClickListener { sendFile() }


//////////////왼쪽 패널 뷰 설정//////////////////
        //다른 액티비티로 이동 리스너
        binding.panelStart.btnCreateRoom.setOnClickListener {
            (requireActivity() as AppCompatActivity).startActivity(
                Intent(activity, NewRoomActivity::class.java)
            )
        }

        //입장한 방 목록 리사이클러뷰
        binding.panelStart.recyclerPanelStart.adapter =
            ChattingFragPanelStartAdapter(requireActivity(), this, chattingFragPanelStartItems)

        getRoom()

//////////////오른쪽 패널 뷰 설정/////////////////
        binding.panelEnd.btnRoomSetting.setOnClickListener {
            (requireActivity() as AppCompatActivity).startActivity(
                Intent(activity, RoomSettingActivity::class.java)
            )
        }

        //방 멤버 목록 리사이클러뷰
        binding.panelEnd.recyclerPanelEnd.adapter =
            ChattingFragPanelEndAdapter(requireActivity(), chattingFragPanelEndItems)

        getRoomMember()
    }
//////////////////////////////////////////////////////////////////////////////////

    fun getChat(){
        //TODO: 완성하기
    }

    private fun sendChat(){
        //TODO: 완성하기
    }

    private fun sendFile(){
        //TODO: 완성하기
    }

    private fun getFriend(){
        //TODO: 완성하기
    }

    private fun getRoom(){
        //TODO: 완성하기
    }

    private fun getRoomMember(){
        //TODO: 완성하기
    }
}