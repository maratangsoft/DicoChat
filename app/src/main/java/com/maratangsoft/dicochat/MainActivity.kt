package com.maratangsoft.dicochat

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.maratangsoft.dicochat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragments = arrayOf(ChatFragment(), FriendsFragment(), MentionFragment(), SettingFragment())
    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        manager.beginTransaction().add(R.id.container, fragments[0]).commit()
        checkPermissions()
        binding.bnv.setOnItemSelectedListener { navigateFragment(it) }
    }

    private fun navigateFragment(menuItem:MenuItem): Boolean{
        val transaction = manager.beginTransaction()
        transaction.hide(fragments[0])
        transaction.remove(fragments[1])
        transaction.remove(fragments[2])
        transaction.remove(fragments[3])

        when (menuItem.itemId){
            R.id.frag_chatting -> transaction.show(fragments[0])
            R.id.frag_friends -> transaction.add(R.id.container, fragments[1])
            R.id.frag_mention -> transaction.add(R.id.container, fragments[2])
            R.id.frag_setting -> transaction.add(R.id.container, fragments[3])
        }
        transaction.commit()

        return true
    }

    //자동 키보드 내리기 콜백메소드
//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//        if(currentFocus is AppCompatEditText) {
//            currentFocus!!.clearFocus()
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    private fun checkPermissions(){
        val wes = android.Manifest.permission.WRITE_EXTERNAL_STORAGE    //< 29
        val aml = android.Manifest.permission.ACCESS_MEDIA_LOCATION     //29 <=
        val res = android.Manifest.permission.READ_EXTERNAL_STORAGE     //< 33
        val rmi = android.Manifest.permission.READ_MEDIA_IMAGES         //33 <=
        val pn = android.Manifest.permission.POST_NOTIFICATIONS         //33 <=

        val GRANTED = PackageManager.PERMISSION_GRANTED

        if (VERSION.SDK_INT < 29) {
            if (checkSelfPermission(wes) != GRANTED){
                permissionLauncher.launch(arrayOf(res, wes))
            }
        }else if (VERSION.SDK_INT < 33) {
            if (checkSelfPermission(wes) != GRANTED){
                permissionLauncher.launch(arrayOf(res, aml))
            }
        }else{
            if (checkSelfPermission(aml) != GRANTED && checkSelfPermission(pn) != GRANTED){
                permissionLauncher.launch(arrayOf(aml, rmi, pn))
            } else if (checkSelfPermission(aml) != GRANTED && checkSelfPermission(pn) == GRANTED){
                permissionLauncher.launch(arrayOf(aml, rmi))
            } else if (checkSelfPermission(aml) == GRANTED && checkSelfPermission(pn) != GRANTED){
                permissionLauncher.launch(arrayOf(pn))
            }
        }
        //TODO: 퍼미션 관련 읽어보기 https://firebase.google.com/docs/cloud-messaging/android/client?authuser=0#request-permission13
    }
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if(permissions.entries.all { it.value }){
            //Toast.makeText(this, "권한 승인하셨습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val bundle = intent?.getBundleExtra("bundle")
        bundle?.let { manager.setFragmentResult("push_request", it) }
    }
}