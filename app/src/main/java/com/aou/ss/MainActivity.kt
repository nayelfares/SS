package com.aou.ss

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.aou.ss.data.ProjectFile
import com.aou.ss.ui.BaseActivity
import com.aou.ss.ui.UploadActivity
import com.aou.ss.vm.FileAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),MainView {
    lateinit var mainViewModel: MainViewModel
    var allMyFiles=ArrayList<ProjectFile>()
    var allSentFiles=ArrayList<ProjectFile>()
    var allReceivedFiles=ArrayList<ProjectFile>()
    lateinit var adapter: FileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel= MainViewModel(this,this)
        events()
    }


    fun events(){
        myFiles.setOnClickListener {
            getMore.setOnRefreshListener {
                mainViewModel.getMyFiles()
            }
            myFiles.isSelected=true
            sent.isSelected=false
            received.isSelected=false
            if (allMyFiles.isEmpty()) {
                getMore.setRefreshing(true)
                mainViewModel.getMyFiles()
            }
            else{
                adapter= FileAdapter(this,allMyFiles)
                content.adapter=adapter
            }
        }
        sent.setOnClickListener {
            getMore.setOnRefreshListener {
                mainViewModel.getSentFiles()
            }
            myFiles.isSelected=false
            sent.isSelected=true
            received.isSelected=false
            if (allSentFiles.isEmpty()) {
                getMore.setRefreshing(true)
                mainViewModel.getSentFiles()
            }
            else{
                adapter= FileAdapter(this,allSentFiles)
                content.adapter=adapter
            }
        }
        received.setOnClickListener {
            getMore.setOnRefreshListener {
                mainViewModel.getReceivedFiles()
            }
            myFiles.isSelected=false
            sent.isSelected=false
            received.isSelected=true
            if (allReceivedFiles.isEmpty()) {
                getMore.setRefreshing(true)
                mainViewModel.getReceivedFiles()
            }
            else{
                adapter= FileAdapter(this,allReceivedFiles)
                content.adapter=adapter
            }
        }
        myFiles.callOnClick()
    }

    fun create(v: View?) {
        startActivity(Intent(this, UploadActivity::class.java))
    }

    override fun getMyFilesOnFail(message: String) {
        getMore.setRefreshing(false)
        showMessage(message)
    }

    override fun getMyFilesOnSuccess(files: ArrayList<ProjectFile>) {
        getMore.setRefreshing(false)
        allMyFiles=files
        adapter= FileAdapter(this,allMyFiles)
        content.adapter=adapter
    }

    override fun getSentFilesOnFail(message: String) {
        getMore.setRefreshing(false)
        showMessage(message)
    }

    override fun getSentFilesOnSuccess(files: ArrayList<ProjectFile>) {
        getMore.setRefreshing(false)
        allSentFiles=files
        adapter= FileAdapter(this,allSentFiles)
        content.adapter=adapter
    }

    override fun getReceivedFilesOnFail(message: String)  {
        getMore.setRefreshing(false)
        showMessage(message)
    }

    override fun getReceivedFilesOnSuccess(files:ArrayList<ProjectFile>) {
        getMore.setRefreshing(false)
        allReceivedFiles=files
        adapter= FileAdapter(this,allReceivedFiles)
        content.adapter=adapter
    }
}