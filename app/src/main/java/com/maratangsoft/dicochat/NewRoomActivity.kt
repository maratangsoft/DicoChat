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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnCreateRoom.setOnClickListener { registerRoom() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun registerRoom(){
        if (binding.etRoomTitle.text == null){
            Toast.makeText(this, "방 이름을 입력해 주세요!", Toast.LENGTH_SHORT).show()
        }
        val roomTitle = binding.etRoomTitle.text.toString()
        //TODO: 완성하기
    }
}