package com.moi.lime.api

import com.moi.lime.vo.*
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query


interface MoiService {
    @GET("music/url")
    fun getMusicUrl(@Query("id") id: String): Flowable<MusicUrlBean>

    @GET("login/cellphone")
    fun signInByPhone(@Query("phone") phoneNumber: String,
                      @Query("password") password: String)
            : Flowable<SignInByPhoneBean>

    @GET("login/refresh")
    fun signInRefresh(): Flowable<OnlyCodeBean>

    @GET("recommend/songs")
    fun fetchRecommendSongs(): Flowable<RecommendSongsEntity>

    @GET("music/url")
    fun fetchMusicUrlById(@Query("id") ids: String): Flowable<MusicUrlsEntity>

}