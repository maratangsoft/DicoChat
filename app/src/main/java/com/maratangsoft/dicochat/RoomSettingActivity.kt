package com.maratangsoft.dicochat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.maratangsoft.dicochat.databinding.ActivityRoomSettingBinding

class RoomSettingActivity : AppCompatActivity() {
    val binding by lazy { ActivityRoomSettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.etRoomTitle.setOnEditorActionListener { _, actionId, _ -> clickDone(actionId) }

        getRoomTitle()
    }

    private fun getRoomTitle(){
        //TODO: 완성하기
    }

    private fun clickDone(actionId:Int): Boolean{
        if (actionId == EditorInfo.IME_ACTION_DONE){
            editRoomTitle()
        }
        return false
    }

    private fun editRoomTitle(){
        //TODO: 완성하기
    }
}