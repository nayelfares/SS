package com.aou.ss.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.aou.ss.R
import com.aou.ss.data.User
import com.aou.ss.vm.SendViewModel
import java.util.*


class SendActivity : BaseActivity() ,SendView{
    lateinit var sendViewModel: SendViewModel
    lateinit var allUsers:ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)
        sendViewModel= SendViewModel(this,this)
        sendViewModel.getUsers()
    }

    override fun getUsersOnFail(message: String) {
        showMessage(message)
    }

    override fun getUsersOnSuccess(users: ArrayList<User>) {
        allUsers=users
        val list=ArrayList<String>()
        for (user in users)
            list.add(user.name)
        val reciever=findViewById<Spinner>(R.id.reciever)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        reciever.adapter=adapter
    }
}