package com.maratangsoft.dicochat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.POST

object RetrofitHelper{
    fun getInstance(baseUrl:String): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface RetrofitService {
    //LoginFragment
    //유저 등록 + 유저정보 받기: dicochat_users INSERT → dicochat_users SELECT / GET 데이터=>JSON


    //ChattingFragment
    //채팅 조회: $roomName_chats SELECT / GET 데이터=>JSON


    //일반채팅 보내기: $roomName_chats INSERT / GET 데이터=>상태메시지


    //파일채팅 보내기: $roomName_chats INSERT / POST 데이터+파일=>상태메시지


    //입장한 채팅방 조회: dicochat_room_member SELECT → dicochat_rooms SELECT / GET 데이터=>JSON


    //채팅방 멤버 조회: dicochat_room_member SELECT → dicochat_users SELECT / GET 데이터=>JSON


    //채팅방 초대+입장: dicochat_users SELECT → dicochat_room_member INSERT / GET 데이터=>JSON / FCM서버로 푸시


    //NewRoomActivity, FriendsFragment
    //채팅방 개설: dicochat_rooms INSERT / POST 데이터+파일=>상태메시지


    //RoomSettingActivity
    //채팅방 이름 수정: dicochat_rooms UPDATE / GET 데이터=>상태메시지


    //FriendsFragment
    //친구 조회: dicochat_friend SELECT / GET 데이터=>JSON


    //FindFriendsActivity
    //친구 등록: dicochat_users SELECT → dicochat_friend INSERT / GET 데이터=>상태메시지


    //MentionActivity
    //멘션 조회: $roomName_chats SELECT / GET 데이터=>JSON


    //필터 목록 조회: $roomName_chats SELECT → dicochat_rooms SELECT / GET 데이터=>JSON


    //SettingFragment
    //프로필 조회: dicochat_users SELECT / GET 데이터=>JSON


    //프로필 수정: dicochat_users UPDATE / POST 데이터+파일=>상태메시지

}