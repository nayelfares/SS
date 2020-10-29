package com.aou.ss.ui

interface LoginView {
    fun onFailer(message:String)
    fun onSuccess(token:String)
}