package com.aou.ss

import com.aou.ss.data.ProjectFile

interface MainView {
     fun getMyFilesOnSuccess(files: ArrayList<ProjectFile>)
     fun getMyFilesOnFail(message: String)
     fun getSentFilesOnSuccess(files: ArrayList<ProjectFile>)
     fun getSentFilesOnFail(message: String)
     fun getReceivedFilesOnSuccess(files:ArrayList<ProjectFile>)
     fun getReceivedFilesOnFail(message: String)

}