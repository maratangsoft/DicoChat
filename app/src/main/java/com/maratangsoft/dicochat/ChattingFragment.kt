package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.maratangsoft.dicochat.databinding.FragmentChattingBinding

class ChattingFragment : Fragment(), MenuProvider {
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

        //툴바 제어를 위한 메뉴프로바이더와 서포트액션바 지정
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        (activity as MainActivity).setSupportActionBar(binding.panelCentral.toolbar)

        binding.panelStart.btnCreateRoom.setOnClickListener { (activity as MainActivity).startActivity(Intent(activity, NewRoomActivity::class.java)) }
        binding.panelEnd.btnRoomSetting.setOnClickListener { (activity as MainActivity).startActivity(Intent(activity, RoomSettingActivity::class.java)) }
        binding.panelEnd.btnShowBs.setOnClickListener { BottomSheetBehavior.from(binding.panelCentral.bsInvite).state = BottomSheetBehavior.STATE_EXPANDED }
    }

    //오른쪽 패널 열기 버튼 (옵션메뉴) 만들기
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_chatting_opt, menu)
    }
    //왼쪽/오른쪽 패널 열기 버튼 동작
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId){
            android.R.id.home -> binding.overlappingPanels.openStartPanel()
            R.id.opt_drawer_right -> binding.overlappingPanels.openEndPanel()
        }
        return true
    }

}