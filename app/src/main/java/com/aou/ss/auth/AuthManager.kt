package com.aou.ss.auth

import android.content.Context
import android.widget.Toast

class AuthManager {
    companion object{
        var token=""
    }
}

fun String.toLink():String{
    return "https://smartandsecureapplication.towarddevelopment.org/$this"
}

fun showToast(context: Context,message:String){
    Toast.makeText(context,message,Toast.LENGTH_LONG).show()
}