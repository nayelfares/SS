package com.aou.ss

import android.content.Context
import com.aou.ss.api.MainAPIManager
import com.aou.ss.auth.AuthManager
import com.aou.ss.data.MyFilesResPonse
import com.aou.ss.data.RequestInterface
import com.aou.ss.data.UserResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(val mainView: MainView,val context: Context) {
    fun getMyFiles() {
        val apiManager= MainAPIManager().provideRetrofitInterface().create(RequestInterface::class.java)
        val loginVar  = apiManager.getMyFiles(AuthManager.token)
        loginVar.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MyFilesResPonse> {
                    override fun onComplete() { }
                    override fun onSubscribe(d: Disposable) { }
                    override fun onNext(t: MyFilesResPonse) {
                        if (t.success)
                            mainView.getMyFilesOnSuccess(t.data)
                        else
                            mainView.getMyFilesOnFail(t.message)                    }
                    override fun onError(e: Throwable) {
                        mainView.getMyFilesOnFail(e.message.toString())
                    }
                })
    }

}