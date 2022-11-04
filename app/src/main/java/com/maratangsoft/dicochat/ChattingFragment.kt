package com.maratangsoft.dicochat

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

        //메뉴프로바이더 객체 생성
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        //왼쪽 패널 열기 버튼 (홈버튼) 만들기
        (activity as MainActivity).setSupportActionBar(binding.panelCentral.toolbar)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_hamburger)


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