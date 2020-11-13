package com.aou.ss.ui

import android.os.Bundle
import com.aou.ss.R
import com.aou.ss.data.User
import com.aou.ss.vm.SendViewModel
import com.aou.ss.vm.UserAdapter
import kotlinx.android.synthetic.main.activity_send.*
import java.util.ArrayList

class SendActivity : BaseActivity(),SendView {
    lateinit var sentViewModel: SendViewModel
    var fileId=0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)
        sentViewModel= SendViewModel(this,this)
        fileId=intent.getLongExtra("fileId",0)
        loading()
        sentViewModel.getUsers()
    }

    override fun getUsersOnFail(message: String) {
        stopLoading()
        showMessage(message)
    }

    override fun getUsersOnSuccess(users: ArrayList<User>) {
        stopLoading()
        usersView.adapter=UserAdapter(this,users,fileId)
    }
}