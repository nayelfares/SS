package com.aou.ss

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aou.ss.data.ProjectFile
import com.aou.ss.ui.BaseActivity
import com.aou.ss.ui.SendActivity
import com.aou.ss.vm.FileAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),MainView {
    lateinit var mainViewModel: MainViewModel
    var allMyFiles=ArrayList<ProjectFile>()
    lateinit var myFilesAdapter: FileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel= MainViewModel(this,this)
        events()
    }


    fun events(){
        myFiles.setOnClickListener {
            myFiles.isSelected=true
            sent.isSelected=false
            received.isSelected=false
            mainViewModel.getMyFiles()
        }
        myFiles.callOnClick()
    }

    fun create(v: View?) {
        startActivity(Intent(this, SendActivity::class.java))
    }

    override fun getMyFilesOnFail(message: String) {
        showMessage(message)
    }

    override fun getMyFilesOnSuccess(files: ArrayList<ProjectFile>) {
        allMyFiles=files
        myFilesAdapter= FileAdapter(this,allMyFiles)
        content.adapter=myFilesAdapter
    }
}