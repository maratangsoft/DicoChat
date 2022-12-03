package com.maratangsoft.dicochat

class ALL{
    companion object{
        var currentUserNo = "" //앱 이용중인 유저 번호
        var currentUserNick = ""
        var currentUserFCMToken = ""

        var currentRoomNo = "" //접속중인 방 번호
        var currentRoomTitle = ""

        val currentRoomMembers = mutableListOf<UserItem>()

        const val BASE_URL = "http://maratangsoft.dothome.co.kr/DicoChatServer/"
    }
}
