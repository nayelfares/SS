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
                        if (t.success){
                            if (t.data.isNullOrEmpty())
                                mainView.getMyFilesOnSuccess(ArrayList())
                            else
                                mainView.getMyFilesOnSuccess(t.data)
                        }
                        else
                            mainView.getMyFilesOnFail(t.message)                    }
                    override fun onError(e: Throwable) {
                        mainView.getMyFilesOnFail(e.message.toString())
                    }
                })
    }

    fun getSentFiles() {
        val apiManager= MainAPIManager().provideRetrofitInterface().create(RequestInterface::class.java)
        val loginVar  = apiManager.getSentFiles(AuthManager.token)
        loginVar.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MyFilesResPonse> {
                    override fun onComplete() { }
                    override fun onSubscribe(d: Disposable) { }
                    override fun onNext(t: MyFilesResPonse) {
                        if (t.success) {
                            if (t.data.isNullOrEmpty())
                                mainView.getSentFilesOnSuccess(ArrayList())
                            else
                                mainView.getSentFilesOnSuccess(t.data)
                        }
                        else
                            mainView.getSentFilesOnFail(t.message)                    }
                    override fun onError(e: Throwable) {
                        mainView.getSentFilesOnFail(e.message.toString())
                    }
                })
    }

    fun getReceivedFiles() {
        val apiManager= MainAPIManager().provideRetrofitInterface().create(RequestInterface::class.java)
        val loginVar  = apiManager.getReceivedFiles(AuthManager.token)
        loginVar.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MyFilesResPonse> {
                    override fun onComplete() { }
                    override fun onSubscribe(d: Disposable) { }
                    override fun onNext(t: MyFilesResPonse) {
                        if (t.success) {
                            if (t.data.isNullOrEmpty())
                                mainView.getReceivedFilesOnSuccess(ArrayList())
                            else
                                mainView.getReceivedFilesOnSuccess(t.data)
                        }
                        else
                            mainView.getReceivedFilesOnFail(t.message)                    }
                    override fun onError(e: Throwable) {
                        mainView.getReceivedFilesOnFail(e.message.toString())
                    }
                })
    }
}