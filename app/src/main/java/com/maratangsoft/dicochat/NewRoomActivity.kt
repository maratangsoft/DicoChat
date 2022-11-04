package com.maratangsoft.dicochat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maratangsoft.dicochat.databinding.ActivityNewRoomBinding

class NewRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityNewRoomBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}