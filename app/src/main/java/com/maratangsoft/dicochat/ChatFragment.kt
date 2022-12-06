package com.maratangsoft.dicochat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.discord.panels.OverlappingPanelsLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.maratangsoft.dicochat.databinding.FragmentChatBinding
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    var chatItems: MutableList<ChatItem> = mutableListOf()
    var roomItems: MutableList<RoomItem> = mutableListOf()
    private lateinit var bsd:BottomSheetDialog

    private var imgPath = ""
    private val retrofitService: RetrofitService by lazy {
        RetrofitHelper.getInstance().create(RetrofitService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("push_request", this) {
                _, bundle -> takePushChats(bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("tttCurrentUserNo", ALL.currentUserNo)
        Log.d("tttCurrentRoomNo", ALL.currentRoomNo)

        //앱 시작시 채팅방 입장을 위해 왼쪽 패널부터 감
        if (ALL.currentRoomNo == "") {
            binding.overlappingPanels.setStartPanelLockState(OverlappingPanelsLayout.LockState.OPEN)
        }

//////////////가운데 패널 뷰 설정////////////////
        //채팅목록 리사이클러뷰
        binding.pCentral.rvCentral.adapter =
            ChatFragAdapter(requireActivity(), chatItems)

        //툴바 아이콘 리스너
        binding.pCentral.toolbar.setNavigationOnClickListener{ binding.overlappingPanels.openStartPanel() }
        binding.pCentral.toolbar.setOnMenuItemClickListener {
            binding.overlappingPanels.openEndPanel()
            true
        }

        //하단 버튼 리스너
        binding.pCentral.btnSendChat.setOnClickListener {
            val mention = checkMention()
            sendChat(mention)
        }
        binding.pCentral.btnSendFile.setOnClickListener { gotoGallery() }
        
        binding.pCentral.rvCentral.setOnTouchListener { v, event -> hideKeyboard(v, event) }

//////////////왼쪽 패널 뷰 설정//////////////////
        //입장한 방 목록 리사이클러뷰
        binding.pStart.rvStart.adapter =
            ChatFragStartAdapter(requireActivity(), this, roomItems)

        //다른 액티비티로 이동 리스너
        binding.pStart.btnRegisterRoom.setOnClickListener {
            (requireActivity() as AppCompatActivity).startActivity(
                Intent(activity, NewRoomActivity::class.java)
            )
        }
//////////////오른쪽 패널 뷰 설정/////////////////
        //방 멤버 목록 리사이클러뷰
        binding.pEnd.rvEnd.adapter =
            ChatFragEndAdapter(requireActivity(), this, ALL.currentRoomMembers)

        getRoom()

        //방 알림 (푸시 주제 구독) 리스너
        binding.pEnd.btnNoti.setOnClickListener { clickBtnNoti() }

        //방 설정 리스너
        binding.pEnd.btnRoomSetting.setOnClickListener {
            (requireActivity() as AppCompatActivity).startActivity(
                Intent(activity, RoomSettingActivity::class.java)
            )
        }
        //초대하기 바텀시트 다이얼로그 리스너
        binding.pEnd.btnShowBs.setOnClickListener {
            val bsDialog = InviteBSFragment(this)
            bsDialog.show(requireActivity().supportFragmentManager, bsDialog.tag)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        getRoom()
        if (ALL.currentRoomNo != "") getRoomMember()
    }

//////////////////////////////////////////////////////////////////////////////////

    private fun getChat(){
        binding.pCentral.toolbar.title = "#${ALL.currentRoomTitle}"
        binding.pEnd.tvRoomTitle.text = "#${ALL.currentRoomTitle}"

        chatItems.clear()
        val adapter = binding.pCentral.rvCentral.adapter
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
                        chatItems.add(item)
                        adapter?.notifyItemInserted(i)
                        binding.pCentral.rvCentral.scrollToPosition(adapter?.itemCount?.minus(1) ?: 0)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<ChatItem>>, t: Throwable) {
                Log.d("tttGetChat", t.message!!)
            }
        })
    }

    private fun checkMention(): String{
        val message = binding.pCentral.etMsg.text
        var mention = ""
        message?.let {
            ALL.currentRoomMembers.forEach {
                if (message.contains("@${it.nickname}")){
                    mention = it.user_no
                }
            }
        }
        return mention
    }

    private fun sendChat(mention:String){
        if (binding.pCentral.etMsg.text.isNullOrEmpty()) return

        val queryMap = mutableMapOf<String, String>()
        queryMap["type"] = "send_chat"
        queryMap["room_no"] = ALL.currentRoomNo
        queryMap["user_no"] = ALL.currentUserNo
        queryMap["message"] = binding.pCentral.etMsg.text.toString()
        queryMap["mentioned"] = mention

        retrofitService.getToPlain(queryMap).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {

                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("tttPanelC-sendChat", t.message!!)
            }
        })
        binding.pCentral.etMsg.setText("")
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
                Log.d("tttSendFile", t.message!!)
            }
        })
    }

    //번들로 넘어온 푸시 받아서 채팅창에 추가
    private fun takePushChats(bundle:Bundle){
        val chatNo = bundle.getString("chat_no")
        val roomNo = bundle.getString("room_no")
        val userNo = bundle.getString("user_no")
        val message = bundle.getString("message")
        val fileUrl = bundle.getString("file_url")
        val mentioned = bundle.getString("mentioned")
        val writeDate = bundle.getString("write_date")
        val nickname = bundle.getString("nickname")
        val userImg = bundle.getString("user_img")

        val pushItem = ChatItem(chatNo!!, roomNo!!, userNo!!, message!!, fileUrl!!, mentioned!!, writeDate!!, null, nickname!!, userImg!!)

        chatItems.add(pushItem)
        val recycler = binding.pCentral.rvCentral
        recycler.adapter?.notifyItemInserted(chatItems.lastIndex)
        recycler.scrollToPosition(recycler.adapter?.itemCount?.minus(1) ?: 0)
    }

    private fun hideKeyboard(v:View, event:MotionEvent): Boolean{
        if (event?.action === MotionEvent.ACTION_DOWN){
            if (v != binding.pCentral.btnSendChat && v != binding.pCentral.btnSendFile){
                requireActivity().currentFocus?.let {
                    val imm: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                    if(it is AppCompatEditText) it.clearFocus()
                }
            }
        }
        return false
    }
/////////왼쪽 패널//////////////////////////////////////////////////

    private fun getRoom(){
        roomItems.clear()
        val adapter = binding.pStart.rvStart.adapter
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
                        roomItems.add(item)
                        adapter?.notifyItemInserted(i)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<RoomItem>>, t: Throwable) {
                Log.d("tttGetRoom", t.message!!)
            }
        })
    }

    //어댑터 연동된 리스너 메소드
    fun clickRoom(position:Int) {
        val item = roomItems[position]
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
        ALL.currentRoomMembers.clear()
        val adapter = binding.pEnd.rvEnd.adapter
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
                        ALL.currentRoomMembers.add(item)
                        adapter?.notifyItemInserted(i)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<UserItem>>, t: Throwable) {
                Log.d("tttGetRoomMember", t.message!!)
            }
        })
    }

    //어댑터 연동된 멤버 상세정보 바텀시트
    fun getProfileBs(position:Int){
        bsd = BottomSheetDialog(requireActivity())
        bsd.setContentView(layoutInflater.inflate(R.layout.bs_profile, null))

        val tvNick = bsd.findViewById<AppCompatTextView>(R.id.tv_nickname)
        val tvUserNo = bsd.findViewById<AppCompatTextView>(R.id.tv_user_no)
        val civUserImg = bsd.findViewById<CircleImageView>(R.id.civ_user_img)

        bsd.show()

        tvNick?.text = ALL.currentRoomMembers[position].nickname
        tvUserNo?.text = "#${ALL.currentRoomMembers[position].user_no}"

        val imgUrl = "${ALL.BASE_URL}${ALL.currentRoomMembers[position].user_img}"
        Glide.with(requireActivity()).load(imgUrl).error(R.drawable.icons8_monkey_164).into(civUserImg!!)
    }

    private fun clickBtnNoti(){
        FirebaseMessaging.getInstance().subscribeToTopic("test").addOnSuccessListener {
            Toast.makeText(requireActivity(), getString(R.string.msg_panelE_subscribe), Toast.LENGTH_SHORT).show()
        }
    }
}