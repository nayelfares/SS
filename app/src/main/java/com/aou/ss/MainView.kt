package com.aou.ss

import com.aou.ss.data.ProjectFile

interface MainView {
     fun getMyFilesOnSuccess(files: ArrayList<ProjectFile>)
     fun getMyFilesOnFail(message: String)

}