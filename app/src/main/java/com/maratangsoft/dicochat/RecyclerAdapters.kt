package com.maratangsoft.dicochat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.maratangsoft.dicochat.databinding.FragmentChattingBsItemBinding
import com.maratangsoft.dicochat.databinding.FragmentChattingDrawerLeftItemBinding
import com.maratangsoft.dicochat.databinding.FragmentChattingDrawerRightItemBinding
import com.maratangsoft.dicochat.databinding.FragmentChattingItemBinding
import com.maratangsoft.dicochat.databinding.FragmentMentionBsItemBinding
import com.maratangsoft.dicochat.databinding.FragmentMentionItemBinding

class ChattingFragAdapter(val context:Context, var items:MutableList<ChatItem>): Adapter<ChattingFragAdapter.ChattingFragVH>(){
    inner class ChattingFragVH(val binding: FragmentChattingItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingFragVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_item, parent, false)
        return ChattingFragVH(FragmentChattingItemBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: ChattingFragVH, position: Int) {
        Glide.with(context).load(items[position].user_img).into(holder.binding.civUserImg)
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvMesssage.text = items[position].message
        holder.binding.tvWriteDate.text = items[position].write_date
    }

    override fun getItemCount() = items.size
}

class ChattingFragBsAdapter(val context:Context, var items:MutableList<UserItem>): Adapter<ChattingFragBsAdapter.ChattingFragBsVH>(){
    inner class ChattingFragBsVH(val binding: FragmentChattingBsItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingFragBsVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_bs_item, parent, false)
        return ChattingFragBsVH(FragmentChattingBsItemBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: ChattingFragBsVH, position: Int) {
        Glide.with(context).load(items[position].user_img).into(holder.binding.civUserImg)
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvUserNo.text = items[position].user_no.toString()
    }

    override fun getItemCount() = items.size
}

class ChattingFragDrawerLeftAdapter(val context:Context, var items:MutableList<RoomItem>): Adapter<ChattingFragDrawerLeftAdapter.ChattingFragDrawerLeftVH>(){
    inner class ChattingFragDrawerLeftVH(val binding:FragmentChattingDrawerLeftItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingFragDrawerLeftVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_drawer_left_item, parent, false)
        return ChattingFragDrawerLeftVH(FragmentChattingDrawerLeftItemBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: ChattingFragDrawerLeftVH, position: Int) {
        Glide.with(context).load(items[position].room_img).into(holder.binding.civRoomImg)
    }

    override fun getItemCount() = items.size
}

class ChattingFragDrawerRightAdapter(val context:Context, var items:MutableList<UserItem>): Adapter<ChattingFragDrawerRightAdapter.ChattingFragDrawerRightVH>(){
    inner class ChattingFragDrawerRightVH(val binding:FragmentChattingDrawerRightItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingFragDrawerRightVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_drawer_right_item, parent, false)
        return ChattingFragDrawerRightVH(FragmentChattingDrawerRightItemBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: ChattingFragDrawerRightVH, position: Int) {
        Glide.with(context).load(items[position].user_img).into(holder.binding.civUserImg)
        holder.binding.tvNickname.text = items[position].nickname
    }

    override fun getItemCount() = items.size
}

class MentionFragmentAdapter(val context:Context, var items:MutableList<ChatItem>): Adapter<MentionFragmentAdapter.MentionFragVH>(){
    inner class MentionFragVH(val binding:FragmentMentionItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentionFragVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_drawer_right_item, parent, false)
        return MentionFragVH(FragmentMentionItemBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: MentionFragVH, position: Int) {
        holder.binding.tvRoomTitle.text = items[position].room_title
        Glide.with(context).load(items[position].user_img).into(holder.binding.civUserImg)
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvWriteDate.text = items[position].write_date
        holder.binding.tvMessage.text = items[position].message
    }

    override fun getItemCount() = items.size
}

class MentionFragmentBsAdapter(val context:Context, var items:MutableList<RoomItem>): Adapter<MentionFragmentBsAdapter.MentionFragBsVH>(){
    inner class MentionFragBsVH(val binding:FragmentMentionBsItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentionFragBsVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_drawer_right_item, parent, false)
        return MentionFragBsVH(FragmentMentionBsItemBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: MentionFragBsVH, position: Int) {
        holder.binding.cbFilter.text = items[position].room_title
    }

    override fun getItemCount() = items.size
}