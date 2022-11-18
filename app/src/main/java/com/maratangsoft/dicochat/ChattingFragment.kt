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
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.discord.panels.OverlappingPanelsLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.maratangsoft.dicochat.databinding.FragmentChattingBinding
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ChattingFragment : Fragment() {
    lateinit var binding: FragmentChattingBinding
    var chattingFragItems: MutableList<ChatItem> = mutableListOf()
    var chattingFragPanelStartItems: MutableList<RoomItem> = mutableListOf()
    var chattingFragPanelEndItems: MutableList<UserItem> = mutableListOf()
    private lateinit var bsd:BottomSheetDialog

    private var imgPath = ""
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
        Log.d("CICO-currentUserNo", ALL.currentUserNo)
        Log.d("CICO-currentRoomNo", ALL.currentRoomNo)

        //앱 시작시 채팅방 입장을 위해 왼쪽 패널부터 감
        if (ALL.currentRoomNo == "") {
            binding.overlappingPanels.setStartPanelLockState(OverlappingPanelsLayout.LockState.OPEN)
        }

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

        //하단 버튼 리스너
        binding.panelCentral.btnSendChat.setOnClickListener { sendChat() }
        binding.panelCentral.btnSendFile.setOnClickListener { gotoGallery() }

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

        //초대하기 바텀시트 다이얼로그 리스너
        binding.panelEnd.btnShowBs.setOnClickListener {
            val bsDialog = ChattingBSFragment(this)
            bsDialog.show(requireActivity().supportFragmentManager, bsDialog.tag)
        }

        //방 멤버 목록 리사이클러뷰
        binding.panelEnd.recyclerPanelEnd.adapter =
            ChattingFragPanelEndAdapter(requireActivity(), this, chattingFragPanelEndItems)

        getRoom()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        getRoom()
        if (ALL.currentRoomNo != "") getRoomMember()
    }
//////////////////////////////////////////////////////////////////////////////////

    private fun getChat(){
        binding.panelCentral.toolbar.title = "#${ALL.currentRoomTitle}"
        binding.panelEnd.tvRoomTitle.text = "#${ALL.currentRoomTitle}"

        chattingFragItems.clear()
        val adapter = binding.panelCentral.recyclerPanelCentral.adapter
        adapter?.notifyDataSetChanged()

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
        queryMap["mentioned"] = "" //TODO: 멘션 기능 넣기

        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    getChat()
                    //TODO: 푸시 기능 넣으면 이거 빼라
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CICO-PanelC-sendChat", t.message!!)
            }
        })
        binding.panelCentral.etMsg.setText("")
    }

    private fun gotoGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

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

            Log.d("CICO-FragC-imgPath", imgPath)
            sendFile()
        }
    }

    private fun sendFile(){
        val dataPart = mutableMapOf<String, String>()
        dataPart["type"] = "send_file"
        dataPart["room_no"] = ALL.currentRoomNo
        dataPart["user_no"] = ALL.currentUserNo

        val file = File(imgPath)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val filePart = MultipartBody.Part.createFormData("img", file.name, requestBody)

        retrofitService.postToPlain(dataPart, filePart).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    Toast.makeText(requireActivity(), R.string.msg_empty_response, Toast.LENGTH_SHORT).show()
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
/////////왼쪽 패널//////////////////////////////////////////////////

    private fun getRoom(){
        chattingFragPanelStartItems.clear()
        val adapter = binding.panelStart.recyclerPanelStart.adapter
        adapter?.notifyDataSetChanged()

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

    //어댑터 연동된 리스너 메소드
    fun clickRoom(position:Int) {
        val item = chattingFragPanelStartItems[position]
        ALL.currentRoomNo = item.room_no
        ALL.currentRoomTitle = item.room_title
        if (ALL.currentRoomNo != "") {
            binding.overlappingPanels.setStartPanelLockState(OverlappingPanelsLayout.LockState.UNLOCKED)
            binding.overlappingPanels.closePanels()
        }

        getChat()
        getRoom()
        getRoomMember()
    }
/////////////오른쪽 패널//////////////////////////////////////////////////

    fun getRoomMember(){
        chattingFragPanelEndItems.clear()
        val adapter = binding.panelEnd.recyclerPanelEnd.adapter
        adapter?.notifyDataSetChanged()

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

    //어댑터 연동된 멤버 상세정보 바텀시트
    fun getProfileBs(position:Int){
        bsd = BottomSheetDialog(requireActivity())
        bsd.setContentView(layoutInflater.inflate(R.layout.fragment_friends_bs, null))

        val tvNick = bsd.findViewById<AppCompatTextView>(R.id.tv_nickname)
        val tvUserNo = bsd.findViewById<AppCompatTextView>(R.id.tv_user_no)
        val civUserImg = bsd.findViewById<CircleImageView>(R.id.civ_user_img)

        bsd.show()

        tvNick?.text = chattingFragPanelEndItems[position].nickname
        tvUserNo?.text = "#${chattingFragPanelEndItems[position].user_no}"

        val imgUrl = "${ALL.BASE_URL}CicoChatServer/${chattingFragPanelEndItems[position].user_img}"
        Glide.with(requireActivity()).load(imgUrl).error(R.drawable.icons8_monkey_164).into(civUserImg!!)
    }
}