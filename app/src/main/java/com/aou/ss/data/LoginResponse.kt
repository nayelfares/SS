package com.aou.ss.data

data class LoginResponse (val success:Boolean,val message:String,val data:LoginData)

data class LoginData(val token:String,val name:String,val id:Long)