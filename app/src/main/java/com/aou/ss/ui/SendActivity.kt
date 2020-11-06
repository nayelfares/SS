package com.aou.ss.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.aou.ss.R
import com.aou.ss.auth.FileUtils
import com.aou.ss.data.User
import com.aou.ss.vm.SendViewModel
import com.blakequ.rsa.FileEncryptionManager
import com.blakequ.rsa.FileUtils.getBytesFromInputStream
import java.io.File
import java.util.*
import kotlinx.android.synthetic.main.activity_send.*
import java.io.FileInputStream


class SendActivity : BaseActivity() ,SendView{
    var publicKey=""
    var privateKey=""
    var name=""
    var type=""
    lateinit var encFile : File
    lateinit var sendViewModel: SendViewModel
    lateinit var mFileEncryptionManager: FileEncryptionManager
    lateinit var allUsers:ArrayList<User>
    val PICKFILE_RESULT_CODE=599
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)
        sendViewModel= SendViewModel(this,this)
        sendViewModel.getUsers()
        mFileEncryptionManager = FileEncryptionManager.getInstance()
    }

    override fun getUsersOnFail(message: String) {
        showMessage(message)
    }

    fun select(v: View?) {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "*/*"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==PICKFILE_RESULT_CODE&&resultCode==Activity.RESULT_OK){
            val filePath=FileUtils.getRealPath(this,data!!.data)!!
            Log.e("filePath",filePath)
            when(FileUtils.getMimeType(filePath).toLowerCase()){
                "application/pdf" -> {
                    type="pdf"
                }
                "image/jpeg"      -> {
                    type="image"
                }
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->{
                    type="word"
                }
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ->{
                    type="excel"
                }
                else->{
                    Log.e("not",FileUtils.getMimeType(filePath).toLowerCase())
                    showMessage("Not supported")
                    return
                }
            }
            showMessage(type)
            val file=File(filePath)
            fileName.text=file.name
            name=file.name
            mFileEncryptionManager.generateKey()
            privateKey = mFileEncryptionManager.privateKey
            publicKey = mFileEncryptionManager.publicKey
            encFile = File("$filesDir/$name")
           mFileEncryptionManager.encryptFileByPublicKey(file, encFile)
        }
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

    fun sendFile(v:View){
        sendViewModel.uploadFile(name,type,encFile,description.text.toString(),publicKey,privateKey)
    }

    override fun uploadFileOnFail(message: String) {
        showMessage(message)
    }

    override fun uploadFileOnSuccess() {
        finish()
    }
}