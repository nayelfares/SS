package com.aou.ss.data

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface RequestInterface {

    @GET
    fun downlload(@Url fileUrl: String?): Call<ResponseBody>

    @POST("user-login")
    fun login(@Query("email") email:String,
              @Query("password") password:String
    ): Observable<LoginResponse>

    @GET("user")
    fun getUsers(): Observable<UserResponse>

    @Headers("accept: application/json")
    @POST("media")
    @Multipart
    fun uploadFile(@Header("Authorization")  token:String,
                   @Query("name") name:String, @Query("type") type:String, @Query("public_key") public_key:String,
                   @Query("private_key") private_key:String, @Query("description") description:String,@Part  path:MultipartBody.Part
    ) : Observable<UploadResponse>

    @GET("media")
    fun getMyFiles(@Header("Authorization")  token:String): Observable<MyFilesResPonse>

    @POST("media/sent")
    fun getSentFiles(@Header("Authorization")  token:String): Observable<MyFilesResPonse>

    @POST("media/recived")
    fun getReceivedFiles(@Header("Authorization")  token:String): Observable<MyFilesResPonse>

    @POST("transaction")
    fun send(@Header("Authorization")  token:String,@Query("reciver_id") reciver_id:Long,
             @Query("file_id") file_id:Long
    ): Observable<UploadResponse>
}