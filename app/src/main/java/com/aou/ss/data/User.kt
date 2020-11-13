package com.aou.ss.data

data class User(val id:Long,val name:String,val photo:String)

data class UserResponse (val success:Boolean,val message:String,val data:ArrayList<User>)

