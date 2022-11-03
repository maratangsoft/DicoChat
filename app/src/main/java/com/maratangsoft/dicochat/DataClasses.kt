package com.maratangsoft.dicochat

data class RoomItem(val room_no:Int, var room_title:String, var room_img:String?, val create_date:String)

data class UserItem(val user_no:Int, var nickname:String, var user_img:String?)

data class ChatItem(
    val chat_no:Int, var message:String, var file_url:String?, val write_date:String,
    val room_no:Int, val room_title:String?,
    val user_no:Int, val nickname:String, val user_img:String?
    )