package com.maratangsoft.dicochat

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

        //왼쪽 드로워 토글버튼 만들기
        val drawerToggle =ActionBarDrawerToggle(activity, binding.layoutDrawerLeft, binding.toolbar, R.string.drawer_open, R.string.drawer_close)
        drawerToggle.syncState()
        binding.layoutDrawerLeft.addDrawerListener(drawerToggle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (binding.layoutDrawerLeft.isDrawerOpen(GravityCompat.START)) binding.layoutDrawerLeft.closeDrawer(GravityCompat.START)
        binding.layoutDrawerLeft.openDrawer(GravityCompat.END)
        return super.onOptionsItemSelected(item)
    }

}