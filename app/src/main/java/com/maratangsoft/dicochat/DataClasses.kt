package com.maratangsoft.dicochat

data class RoomItem(
    val room_no: String,
    var room_title: String,
    var room_img: String,
    val create_date: String
)

data class UserItem(
    val user_no: String,
    val friend_no: String?,
    var nickname: String,
    var user_img: String
)

data class ChatItem(
    val chat_no: String,
    val room_no: String,
    val user_no: String,
    val message: String?,
    val file_url: String?,
    val mentioned: String?,
    val write_date: String,
    var room_title: String?,
    var nickname: String,
    var user_img: String
)
