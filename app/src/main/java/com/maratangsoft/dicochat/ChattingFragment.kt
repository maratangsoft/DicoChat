package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.maratangsoft.dicochat.databinding.FragmentChattingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingFragment : Fragment() {
    lateinit var binding: FragmentChattingBinding
    var chattingFragItems: MutableList<ChatItem> = mutableListOf()
    var chattingFragBsItems: MutableList<UserItem> = mutableListOf()
    var chattingFragPanelStartItems: MutableList<RoomItem> = mutableListOf()
    var chattingFragPanelEndItems: MutableList<UserItem> = mutableListOf()
    private val retrofitService: RetrofitService by lazy {
        RetrofitHelper.getInstance().create(RetrofitService::class.java) }

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

        //채팅목록 리사이클러뷰
        binding.panelCentral.recyclerPanelCentral.adapter =
            ChattingFragAdapter(requireActivity(), chattingFragItems)

        getChat()

        //보내기 버튼 리스너
        binding.panelCentral.btnSendChat.setOnClickListener { sendChat() }
        binding.panelCentral.btnSendFile.setOnClickListener { sendFile() }

        //바텀시트 다이얼로그 리스너
        binding.panelEnd.btnShowBs.setOnClickListener {
            val bsd = BottomSheetDialog(requireActivity())
            bsd.setContentView(layoutInflater.inflate(R.layout.fragment_chatting_bs_dialog, null))

            bsd.findViewById<RecyclerView>(R.id.recycler_chatting_bs)?.adapter =
                ChattingFragBsAdapter(requireActivity(), this, chattingFragBsItems)

            getFriend()
            bsd.show()
        }

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
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_chat"
        queryMap["room_no"] = ALL.currentRoomNo
        queryMap["page"] = "1"

        retrofitService.getToJsonChat(queryMap).enqueue(object : Callback<MutableList<ChatItem>> {
            override fun onResponse(
                call: Call<MutableList<ChatItem>>,
                response: Response<MutableList<ChatItem>>
            ) {
                response.body()?.let {
                    val result = it

                }
            }

            override fun onFailure(call: Call<MutableList<ChatItem>>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    private fun sendChat(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "send_chat"
        queryMap["room_no"] = ALL.currentRoomNo
        queryMap["user_no"] = ALL.currentUserNo
        queryMap["message"] = binding.panelCentral.etMsg.text.toString()

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

    private fun sendFile(){
        //TODO: !!
    }

    private fun getFriend(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_friend"
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

    private fun getRoom(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_room"
        queryMap["user_no"] = ALL.currentUserNo

        retrofitService.getToJsonRoom(queryMap).enqueue(object : Callback<MutableList<RoomItem>> {
            override fun onResponse(
                call: Call<MutableList<RoomItem>>,
                response: Response<MutableList<RoomItem>>
            ) {
                response.body()?.let {
                    val result = it

                }
            }

            override fun onFailure(call: Call<MutableList<RoomItem>>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    private fun getRoomMember(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_room_member"
        queryMap["room_no"] = ALL.currentRoomNo

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

    fun inviteUser(){
        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "invite_user"
        queryMap["user_no"] = ALL.currentUserNo
        queryMap["room_no"] = ALL.currentRoomNo

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
}