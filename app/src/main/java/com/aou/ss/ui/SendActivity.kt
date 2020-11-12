package com.aou.ss.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import kotlinx.android.synthetic.main.activity_send.*
import org.jetbrains.anko.doAsync
import java.io.File
import java.util.*


class SendActivity : BaseActivity() ,SendView{
    var publicKey=""
    var privateKey=""
    var name=""
    var type=""
    lateinit var encFile : File
    lateinit var sendViewModel: SendViewModel
    lateinit var mFileEncryptionManager: FileEncryptionManager
    lateinit var allUsers:ArrayList<User>
    var fileUri:Uri?=null
    val PICKFILE_RESULT_CODE=599
    val PERMISSION_CODE  =4004
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
        if (checkPermission(PERMISSION_CODE)) {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
        }
    }

    fun open(v:View){
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val filePath=FileUtils.getRealPath(this,fileUri)!!
            intent.setDataAndType(fileUri, FileUtils.getMimeType(filePath))
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showMessage("no available app")
        }
    }

    fun cancel(v:View){
        fileName.text="No file selected"
        close.visibility=View.GONE
        open.visibility=View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==PICKFILE_RESULT_CODE&&resultCode==Activity.RESULT_OK){
            fileUri=data?.data
            val filePath=FileUtils.getRealPath(this,data!!.data)!!
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
            val file=File(filePath)
            fileName.text=file.name
            name=file.name
            mFileEncryptionManager.generateKey()
            privateKey = mFileEncryptionManager.privateKey
            publicKey = mFileEncryptionManager.publicKey
            encFile = File("$filesDir/$name")
            doAsync {
                mFileEncryptionManager.encryptFileByPublicKey(file, encFile)
            }
            close.visibility=View.VISIBLE
            open.visibility=View.VISIBLE
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
        if (close.visibility==View.VISIBLE) {
            loading()
            sendViewModel.uploadFile(name, type, encFile, description.text.toString(), publicKey, privateKey)
        }
        else
            showMessage("no file selected")
    }

    override fun uploadFileOnFail(message: String) {
        stopLoading()
        showMessage(message)
    }

    override fun uploadFileOnSuccess() {
        stopLoading()
        finish()
    }

    private fun checkPermission(code: Int): Boolean {
        if (!(checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED)
        ) {
            permissionDialog(code)
            return false
        }else{
            return true
        }

    }

    private fun permissionDialog(code: Int) {
            requestPermissions(
                    arrayOf(
                            "android.permission.READ_EXTERNAL_STORAGE",
                            "android.permission.WRITE_EXTERNAL_STORAGE"
                    ), code
            )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==PERMISSION_CODE &&grantResults.isNotEmpty()){
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
        }
    }
}