package com.aou.ss.ui

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aou.ss.R

abstract class BaseActivity: AppCompatActivity() {
    var dialog: Dialog?=null
    fun showMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    fun loading(){
        dialog= Dialog(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog?.setCancelable(false);
        dialog?.setContentView(R.layout.loading_dialog);
        dialog?.show()
    }

    fun stopLoading(){
        dialog?.dismiss()
    }
}