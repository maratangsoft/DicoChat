package com.maratangsoft.dicochat

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.maratangsoft.dicochat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var fragments: Array<Fragment?> = arrayOf(ChattingFragment(), null, null, null)
    private val manager = supportFragmentManager

    private val permissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
        if(it[permissions[0]] == true){
            Toast.makeText(this, "권한 확인됨", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        manager.beginTransaction().add(R.id.container, fragments[0]!!).commit()
        binding.bnv.setOnItemSelectedListener { clickBnv(it) }

        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED){
            permissionLauncher.launch(permissions)
        }
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

    private fun clickBnv(menuItem: MenuItem): Boolean {
        val transaction = manager.beginTransaction()
        for (i in fragments) if(i != null) transaction.hide(i)

        when (menuItem.itemId){
            R.id.bnv_chatting -> {
                transaction.show(fragments[0]!!)

            }R.id.bnv_friends -> {
                if (fragments[1] == null){
                    fragments[1] = FriendsFragment()
                    transaction.add(R.id.container, fragments[1]!!)
                }
                transaction.show(fragments[1]!!)

            }R.id.bnv_mention -> {
                if (fragments[2] == null){
                    fragments[2] = MentionFragment()
                    transaction.add(R.id.container, fragments[2]!!)
                }
                transaction.show(fragments[2]!!)

            }R.id.bnv_setting -> {
                if (fragments[3] == null){
                    fragments[3] = SettingFragment()
                    transaction.add(R.id.container, fragments[3]!!)
                }
                transaction.show(fragments[3]!!)
            }
        }
        transaction.commit()
        return true
    }
}