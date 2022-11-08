package com.maratangsoft.dicochat

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.maratangsoft.dicochat.databinding.FragmentMentionBinding

class MentionFragment : Fragment() {
    lateinit var binding: FragmentMentionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMentionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //바텀시트 다이얼로그 리스너
        binding.toolbar.setOnMenuItemClickListener {
            val bsd = BottomSheetDialog(requireActivity())
            bsd.setContentView(layoutInflater.inflate(R.layout.fragment_mention_bs_dialog, null))
            bsd.show()
            true
        }
    }
}