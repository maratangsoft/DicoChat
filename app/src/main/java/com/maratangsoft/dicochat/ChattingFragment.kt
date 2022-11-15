package com.maratangsoft.dicochat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.maratangsoft.dicochat.databinding.FragmentChattingBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ChattingFragment : Fragment() {
    lateinit var binding: FragmentChattingBinding
    private lateinit var bsd: BottomSheetDialog
    var chattingFragItems: MutableList<ChatItem> = mutableListOf()
    private var chattingFragBsItems: MutableList<UserItem> = mutableListOf()
    var chattingFragPanelStartItems: MutableList<RoomItem> = mutableListOf()
    var chattingFragPanelEndItems: MutableList<UserItem> = mutableListOf()

    private var imgPath = ""
    private val retrofitService: RetrofitService by lazy {
        RetrofitHelper.getInstance().create(RetrofitService::class.java) }

    //갤러리 앱에서 사진주소 가지고 와서 절대주소로 변환해주는 리절트런처
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode != Activity.RESULT_CANCELED){
            val uri = result.data?.data
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val loader = CursorLoader(requireActivity(), uri!!, proj, null, null, null)
            val cursor = loader.loadInBackground()
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            imgPath = cursor.getString(columnIndex)
            cursor.close()
            Log.d("CICOCHAT", imgPath)
        }
    }

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
        binding.panelCentral.toolbar.setNavigationOnClickListener{ binding.overlappingPanels.openStartPanel() }
        binding.panelCentral.toolbar.setOnMenuItemClickListener {
            binding.overlappingPanels.openEndPanel()
            true
        }

        //채팅목록 리사이클러뷰
        binding.panelCentral.recyclerPanelCentral.adapter =
            ChattingFragAdapter(requireActivity(), chattingFragItems)

        //보내기 버튼 리스너
        binding.panelCentral.btnSendChat.setOnClickListener { sendChat() }
        binding.panelCentral.btnSendFile.setOnClickListener { sendFile() }

//////////////왼쪽 패널 뷰 설정//////////////////
        //다른 액티비티로 이동 리스너
        binding.panelStart.btnRegisterRoom.setOnClickListener {
            (requireActivity() as AppCompatActivity).startActivity(
                Intent(activity, NewRoomActivity::class.java)
            )
        }

        //입장한 방 목록 리사이클러뷰
        binding.panelStart.recyclerPanelStart.adapter =
            ChattingFragPanelStartAdapter(requireActivity(), this, chattingFragPanelStartItems)

//////////////오른쪽 패널 뷰 설정/////////////////
        binding.panelEnd.btnRoomSetting.setOnClickListener {
            (requireActivity() as AppCompatActivity).startActivity(
                Intent(activity, RoomSettingActivity::class.java)
            )
        }

        //바텀시트 다이얼로그 리스너
        binding.panelEnd.btnShowBs.setOnClickListener {
            bsd = BottomSheetDialog(requireActivity())
            bsd.setContentView(layoutInflater.inflate(R.layout.fragment_chatting_bs_dialog, null))

            bsd.findViewById<RecyclerView>(R.id.recycler_chatting_bs)?.adapter = ChattingFragBsAdapter(requireActivity(), this, chattingFragBsItems)

            getFriend()
            bsd.show()
        }

        //방 멤버 목록 리사이클러뷰
        binding.panelEnd.recyclerPanelEnd.adapter =
            ChattingFragPanelEndAdapter(requireActivity(), chattingFragPanelEndItems)
    }

    override fun onResume() {
        super.onResume()
        getChat()
        getRoom()
        getRoomMember()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            getChat()
            getRoom()
            getRoomMember()
        }
    }
//////////////////////////////////////////////////////////////////////////////////

    fun getChat(){
        chattingFragItems.clear()
        val adapter = binding.panelCentral.recyclerPanelCentral.adapter
        adapter?.notifyItemRangeRemoved(0, adapter.itemCount - 1)

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
                    it.forEachIndexed{ i, item ->
                        chattingFragItems.add(item)
                        adapter?.notifyItemInserted(i)
                    }
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
                    if (it == "fail")
                        Toast.makeText(requireActivity(), R.string.error_empty_response, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    private fun sendFile(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)

        val dataPart = mutableMapOf<String, String>()
        dataPart["type"] = "send_file"
        dataPart["room_no"] = ALL.currentRoomNo
        dataPart["user_no"] = ALL.currentUserNo

        val file = File(imgPath)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val filePart = MultipartBody.Part.createFormData("file_url", file.name, requestBody)

        retrofitService.postToPlain(dataPart, filePart).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    Toast.makeText(requireActivity(), R.string.error_empty_response, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    private fun takePushChats(){
        //TODO: 푸시 받아서 채팅목록 업데이트
    }

    private fun getFriend(){
        chattingFragBsItems.clear()
        val adapter = bsd.findViewById<RecyclerView>(R.id.recycler_chatting_bs)?.adapter
        adapter?.notifyItemRangeRemoved(0, adapter.itemCount - 1)

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
                        chattingFragBsItems.add(item)
                        adapter?.notifyItemInserted(i)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<UserItem>>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    private fun getRoom(){
        chattingFragPanelStartItems.clear()
        val adapter = binding.panelStart.recyclerPanelStart.adapter
        adapter?.notifyItemRangeRemoved(0, adapter.itemCount - 1)

        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_room"
        queryMap["user_no"] = ALL.currentUserNo

        retrofitService.getToJsonRoom(queryMap).enqueue(object : Callback<MutableList<RoomItem>> {
            override fun onResponse(
                call: Call<MutableList<RoomItem>>,
                response: Response<MutableList<RoomItem>>
            ) {
                response.body()?.let {
                    it.forEachIndexed{ i, item ->
                        chattingFragPanelStartItems.add(item)
                        adapter?.notifyItemInserted(i)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<RoomItem>>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    private fun getRoomMember(){
        chattingFragPanelEndItems.clear()
        val adapter = binding.panelEnd.recyclerPanelEnd.adapter
        adapter?.notifyItemRangeRemoved(0, adapter.itemCount - 1)

        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "get_room_member"
        queryMap["room_no"] = ALL.currentRoomNo

        retrofitService.getToJsonUser(queryMap).enqueue(object : Callback<MutableList<UserItem>> {
            override fun onResponse(
                call: Call<MutableList<UserItem>>,
                response: Response<MutableList<UserItem>>
            ) {
                response.body()?.let {
                    it.forEachIndexed{ i, item ->
                        chattingFragPanelEndItems.add(item)
                        adapter?.notifyItemInserted(i)
                    }
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
                    if (it == "fail")
                        Toast.makeText(requireActivity(), R.string.error_empty_response, Toast.LENGTH_SHORT).show()
                    else
                        takePushInvite()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICOCHAT", t.message!!)
            }
        })
    }

    fun takePushInvite(){
        //TODO: 푸시로 멤버리스트 업데이트
    }
}