package com.aou.ss.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RequestInterface {
    @POST("user-login")
    fun login(@Query("email") email:String,
                       @Query("password") password:String
    ): Observable<LoginResponse>

    @GET("user")
    fun getUsers(): Observable<UserResponse>
}