package com.maratangsoft.dicochat

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import com.maratangsoft.dicochat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragments = arrayOf(ChattingFragment(), FriendsFragment(), MentionFragment(), SettingFragment())
    private val manager = supportFragmentManager

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if(permissions.entries.all { it.value }){
            Log.d("CICO-MA", "권한 승인")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        manager.beginTransaction().add(R.id.container, fragments[0]).commit()

        binding.bnv.setOnItemSelectedListener { navigateFragment(it) }

        checkPermissions()
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
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean { //해당 뷰 아닌 곳을 터치시 발동
        if (event?.action === MotionEvent.ACTION_DOWN) { //손가락이 눌렀을 때
            val view = currentFocus
            if (view is AppCompatEditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun checkPermissions(){
        val res = android.Manifest.permission.READ_EXTERNAL_STORAGE     //<=29
        val wes = android.Manifest.permission.WRITE_EXTERNAL_STORAGE    //<=32
        val aml = android.Manifest.permission.ACCESS_MEDIA_LOCATION     //29<=
        val rmi = android.Manifest.permission.READ_MEDIA_IMAGES         //33<=

        if (VERSION.SDK_INT <= 28) {
            if (checkSelfPermission(wes) == PackageManager.PERMISSION_DENIED){
                permissionLauncher.launch(arrayOf(res, wes))
            }
        }else if (VERSION.SDK_INT <= 32) {
            if (checkSelfPermission(wes) == PackageManager.PERMISSION_DENIED){
                permissionLauncher.launch(arrayOf(res, wes, aml))
            }
        }else {
            if (checkSelfPermission(aml) == PackageManager.PERMISSION_DENIED){
                permissionLauncher.launch(arrayOf(aml, rmi))
            }
        }
    }
}