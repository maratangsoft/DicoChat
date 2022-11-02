package com.maratangsoft.dicochat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.maratangsoft.dicochat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragments: Array<Fragment?> = arrayOf(ChattingFragment(), null, null, null)
    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        manager.beginTransaction().add(R.id.container, fragments[0]!!).commit()
        binding.bnv.setOnClickListener { clickBnv(it) }
    }

    private fun clickBnv(view:View){
        val transaction = manager.beginTransaction()
        if(fragments[0] != null) transaction.hide(fragments[0]!!);
        if(fragments[1] != null) transaction.hide(fragments[1]!!);
        if(fragments[2] != null) transaction.hide(fragments[2]!!);
        if(fragments[3] != null) transaction.hide(fragments[3]!!);
        if(fragments[4] != null) transaction.hide(fragments[4]!!);

        when (view.id){
        }
    }
}