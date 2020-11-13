package com.aou.ss.ui

import com.aou.ss.data.User
import java.util.ArrayList

interface UploadView {
    fun getUsersOnSuccess(users: ArrayList<User>)

    fun getUsersOnFail(message: String)
    fun uploadFileOnFail(message: String)
    fun uploadFileOnSuccess()

}