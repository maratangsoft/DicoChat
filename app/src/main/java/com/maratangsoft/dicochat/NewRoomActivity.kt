package com.maratangsoft.dicochat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.maratangsoft.dicochat.databinding.ActivityNewRoomBinding

class NewRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityNewRoomBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnCreateRoom.setOnClickListener { registerRoom() }
    }

    private fun registerRoom(){
        if (binding.etRoomTitle.text == null){
            Toast.makeText(this, R.string.error_act_nr_no_input, Toast.LENGTH_SHORT).show()
        }
        val roomTitle = binding.etRoomTitle.text.toString()
        //TODO: 완성하기
    }
}