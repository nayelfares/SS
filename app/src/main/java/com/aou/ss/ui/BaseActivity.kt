package com.aou.ss.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

    fun showMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
}