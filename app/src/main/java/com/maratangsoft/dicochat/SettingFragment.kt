package com.maratangsoft.dicochat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maratangsoft.dicochat.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.civUserImg.setOnClickListener { editUserImg() }
        binding.tvNickname.setOnClickListener { editNickname() }

        getProfile()
    }

    private fun getProfile(){
        //TODO: complete
    }

    private fun editNickname(){
        //TODO: complete
    }

    private fun editUserImg(){
        //TODO: complete
    }

}