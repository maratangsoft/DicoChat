package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.maratangsoft.dicochat.databinding.FragmentChattingBinding

class ChattingFragment : Fragment() {
    lateinit var binding: FragmentChattingBinding

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
            bsd.show()
        }

        //다른 액티비티로 이동 리스너
        binding.panelStart.btnCreateRoom.setOnClickListener { (requireActivity() as AppCompatActivity).startActivity(Intent(activity, NewRoomActivity::class.java)) }
        binding.panelEnd.btnRoomSetting.setOnClickListener { (requireActivity() as AppCompatActivity).startActivity(Intent(activity, RoomSettingActivity::class.java)) }

    }
}