package com.maratangsoft.dicochat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.maratangsoft.dicochat.databinding.FragmentChattingBsItemBinding
import com.maratangsoft.dicochat.databinding.FragmentChattingItemBinding
import com.maratangsoft.dicochat.databinding.FragmentChattingPanelEndItemBinding
import com.maratangsoft.dicochat.databinding.FragmentChattingPanelStartItemBinding
import com.maratangsoft.dicochat.databinding.FragmentMentionItemBinding

class ChattingFragAdapter(val context:Context, var items:MutableList<ChatItem>): Adapter<ChattingFragAdapter.ChattingFragVH>(){
    inner class ChattingFragVH(val binding: FragmentChattingItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingFragVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_item, parent, false)
        return ChattingFragVH(FragmentChattingItemBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: ChattingFragVH, position: Int) {
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvMesssage.text = items[position].message
        holder.binding.tvWriteDate.text = items[position].write_date

        val imgUrl = "${ALL.BASE_URL}CicoChatServer/${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }
    override fun getItemCount() = items.size
}

class ChattingFragPanelStartAdapter(val context:Context, val hostFragment: ChattingFragment, var items:MutableList<RoomItem>): Adapter<ChattingFragPanelStartAdapter.ChattingFragPanelStartVH>(){
    inner class ChattingFragPanelStartVH(val binding:FragmentChattingPanelStartItemBinding): ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { hostFragment.clickRoom(adapterPosition) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingFragPanelStartVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_panel_start_item, parent, false)
        return ChattingFragPanelStartVH(FragmentChattingPanelStartItemBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: ChattingFragPanelStartVH, position: Int) {
        holder.binding.tvRoomTitle.text = items[position].room_title
        holder.binding.vPointer.visibility = if (items[position].room_no == ALL.currentRoomNo) View.VISIBLE
                                            else View.INVISIBLE

        val imgUrl = "${ALL.BASE_URL}CicoChatServer/${items[position].room_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civRoomImg)
    }
    override fun getItemCount() = items.size
}

class ChattingFragPanelEndAdapter(val context:Context, val hostFragment: ChattingFragment, var items:MutableList<UserItem>): Adapter<ChattingFragPanelEndAdapter.ChattingFragPanelEndVH>(){
    inner class ChattingFragPanelEndVH(val binding:FragmentChattingPanelEndItemBinding): ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { hostFragment.getProfileBs(adapterPosition) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingFragPanelEndVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_panel_end_item, parent, false)
        return ChattingFragPanelEndVH(FragmentChattingPanelEndItemBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: ChattingFragPanelEndVH, position: Int) {
        holder.binding.tvNickname.text = items[position].nickname

        val imgUrl = "${ALL.BASE_URL}CicoChatServer/${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }
    override fun getItemCount() = items.size
}

class ChattingFragBsAdapter(val context:Context, val hostFragment: ChattingBSFragment, var items:MutableList<UserItem>): Adapter<ChattingFragBsAdapter.ChattingFragBsVH>(){
    inner class ChattingFragBsVH(val binding: FragmentChattingBsItemBinding): ViewHolder(binding.root){
        init {
            binding.btnInvite.setOnClickListener { hostFragment.inviteUser(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingFragBsVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_bs_item, parent, false)
        return ChattingFragBsVH(FragmentChattingBsItemBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: ChattingFragBsVH, position: Int) {
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvUserNo.text = items[position].friend_no

        val imgUrl = "${ALL.BASE_URL}CicoChatServer/${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }
    override fun getItemCount() = items.size
}

class FriendsFragmentAdapter(val context:Context, val hostFragment: FriendsFragment, var items:MutableList<UserItem>): Adapter<FriendsFragmentAdapter.FriendsFragmentVH>(){
    inner class FriendsFragmentVH(val binding:FragmentChattingPanelEndItemBinding): ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { hostFragment.getProfileBs(adapterPosition) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsFragmentVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_chatting_panel_end_item, parent, false)
        return FriendsFragmentVH(FragmentChattingPanelEndItemBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: FriendsFragmentVH, position: Int) {
        holder.binding.tvNickname.text = items[position].nickname

        val imgUrl = "${ALL.BASE_URL}CicoChatServer/${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }
    override fun getItemCount() = items.size
}

class MentionFragmentAdapter(val context:Context, var items:MutableList<ChatItem>): Adapter<MentionFragmentAdapter.MentionFragVH>(){
    inner class MentionFragVH(val binding:FragmentMentionItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentionFragVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fragment_mention_item, parent, false)
        return MentionFragVH(FragmentMentionItemBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: MentionFragVH, position: Int) {
        holder.binding.tvRoomTitle.text = items[position].room_title
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvWriteDate.text = items[position].write_date
        holder.binding.tvMessage.text = items[position].message

        val imgUrl = "${ALL.BASE_URL}CicoChatServer/${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }

    override fun getItemCount() = items.size
}