package com.maratangsoft.dicochat

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

object RetrofitHelper{
    fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ALL.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface RetrofitService {
    //GET방식, String 리스폰스
    //get_user_no, send_chat, invite_user, get_room_title, set_room_title, get_friend, register_friend, set_nickname
    @GET("CicoChatServer/GETtoPlain.php")
    fun getToPlain(
        @QueryMap queries: MutableMap<String,String>
    ): Call<String>

    //POST방식, String 리스폰스
    //send_file, register_room, set_user_img
    @Multipart
    @POST("CicoChatServer/POSTtoPlain.php")
    fun postToPlain(
        @PartMap dataPart: MutableMap<String,String>, @Part filePart: MultipartBody.Part
    ): Call<String>

    //GET방식, MutableList<UserItem> 리스폰스
    //get_room_member, get_friend, get_profile
    @GET("CicoChatServer/GETtoJSON.php")
    fun getToJsonUser(
        @QueryMap queries: MutableMap<String,String>
    ): Call<MutableList<UserItem>>

    //GET방식, MutableList<RoomItem> 리스폰스
    //get_room
    @GET("CicoChatServer/GETtoJSON.php")
    fun getToJsonRoom(
        @QueryMap queries: MutableMap<String,String>
    ): Call<MutableList<RoomItem>>

    //GET방식, MutableList<ChatItem> 리스폰스
    //get_chat, get_mention
    @GET("CicoChatServer/GETtoJSON.php")
    fun getToJsonChat(
        @QueryMap queries: MutableMap<String,String>
    ): Call<MutableList<ChatItem>>
}