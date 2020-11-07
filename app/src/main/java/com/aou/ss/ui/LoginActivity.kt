package com.aou.ss.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.aou.ss.MainActivity
import com.aou.ss.R
import com.aou.ss.auth.AuthManager
import com.aou.ss.vm.LoginViewModel

class LoginActivity : BaseActivity() ,LoginView {
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel= LoginViewModel(this,this)
    }

    fun login(v: View?) {
        val email: AppCompatEditText =findViewById(R.id.email)
        val password: AppCompatEditText =findViewById(R.id.password)
        loginViewModel.login(email.text.toString(),password.text.toString())
        loading()
    }

    override fun onFailer(message: String) {
        stopLoading()
        showMessage(message)

    }

    override fun onSuccess(token: String) {
        stopLoading()
        AuthManager.token="Bearer $token"
        startActivity(Intent(this, MainActivity::class.java))
    }

}