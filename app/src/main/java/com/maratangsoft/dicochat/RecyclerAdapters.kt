package com.maratangsoft.dicochat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.maratangsoft.dicochat.databinding.ItemChatBinding
import com.maratangsoft.dicochat.databinding.ItemInviteBinding
import com.maratangsoft.dicochat.databinding.ItemMentionBinding
import com.maratangsoft.dicochat.databinding.ItemRoomBinding
import com.maratangsoft.dicochat.databinding.ItemUserBinding

//TODO: 그림 표시용 뷰홀더 따로 만들어야 함
class ChatFragAdapter(val context:Context, var items:MutableList<ChatItem>): Adapter<ChatFragAdapter.ChatFragVH>(){
    inner class ChatFragVH(val binding: ItemChatBinding): ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatFragVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatFragVH(ItemChatBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: ChatFragVH, position: Int) {
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvMesssage.text = items[position].message
        holder.binding.tvWriteDate.text = items[position].write_date

        val imgUrl = "${ALL.BASE_URL}${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }
    override fun getItemCount() = items.size
}

class ChatFragStartAdapter(val context:Context, val hostFragment: ChatFragment, var items:MutableList<RoomItem>): Adapter<ChatFragStartAdapter.ChatFragStartVH>(){
    inner class ChatFragStartVH(val binding:ItemRoomBinding): ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { hostFragment.clickRoom(adapterPosition) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatFragStartVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
        return ChatFragStartVH(ItemRoomBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: ChatFragStartVH, position: Int) {
        holder.binding.tvRoomTitle.text = items[position].room_title
        holder.binding.vPointer.visibility = if (items[position].room_no == ALL.currentRoomNo) View.VISIBLE
                                            else View.INVISIBLE

        val imgUrl = "${ALL.BASE_URL}${items[position].room_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civRoomImg)
    }
    override fun getItemCount() = items.size
}

class ChatFragEndAdapter(val context:Context, val hostFragment: ChatFragment, var items:MutableList<UserItem>): Adapter<ChatFragEndAdapter.ChatFragEndVH>(){
    inner class ChatFragEndVH(val binding:ItemUserBinding): ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { hostFragment.getProfileBs(adapterPosition) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatFragEndVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ChatFragEndVH(ItemUserBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: ChatFragEndVH, position: Int) {
        holder.binding.tvNickname.text = items[position].nickname

        val imgUrl = "${ALL.BASE_URL}${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }
    override fun getItemCount() = items.size
}

class ChatFragBsAdapter(val context:Context, val hostFragment: InviteBSFragment, var items:MutableList<UserItem>): Adapter<ChatFragBsAdapter.ChatFragBsVH>(){
    inner class ChatFragBsVH(val binding: ItemInviteBinding): ViewHolder(binding.root){
        init {
            binding.btnInvite.setOnClickListener { hostFragment.inviteUser(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatFragBsVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_invite, parent, false)
        return ChatFragBsVH(ItemInviteBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: ChatFragBsVH, position: Int) {
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvUserNo.text = items[position].friend_no

        val imgUrl = "${ALL.BASE_URL}${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }
    override fun getItemCount() = items.size
}

class FriendsFragAdapter(val context:Context, val hostFragment: FriendsFragment, var items:MutableList<UserItem>): Adapter<FriendsFragAdapter.FriendsFragVH>(){
    inner class FriendsFragVH(val binding:ItemUserBinding): ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { hostFragment.getProfileBs(adapterPosition) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsFragVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return FriendsFragVH(ItemUserBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: FriendsFragVH, position: Int) {
        holder.binding.tvNickname.text = items[position].nickname

        val imgUrl = "${ALL.BASE_URL}${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }
    override fun getItemCount() = items.size
}

class MentionFragAdapter(val context:Context, var items:MutableList<ChatItem>): Adapter<MentionFragAdapter.MentionFragVH>(){
    inner class MentionFragVH(val binding:ItemMentionBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentionFragVH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_mention, parent, false)
        return MentionFragVH(ItemMentionBinding.bind(itemView))
    }
    override fun onBindViewHolder(holder: MentionFragVH, position: Int) {
        holder.binding.tvRoomTitle.text = "#${items[position].room_title}"
        holder.binding.tvNickname.text = items[position].nickname
        holder.binding.tvWriteDate.text = items[position].write_date
        holder.binding.tvMessage.text = items[position].message

        val imgUrl = "${ALL.BASE_URL}${items[position].user_img}"
        Glide.with(context).load(imgUrl).error(R.drawable.icons8_monkey_164).into(holder.binding.civUserImg)
    }

    override fun getItemCount() = items.size
}