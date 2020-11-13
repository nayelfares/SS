package com.aou.ss.vm

import android.content.Context
import com.aou.ss.api.MainAPIManager
import com.aou.ss.data.RequestInterface
import com.aou.ss.data.UserResponse
import com.aou.ss.ui.SendView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SendViewModel(val sendView: SendView,val context: Context) {
    fun getUsers() {
        val apiManager= MainAPIManager().provideRetrofitInterface().create(RequestInterface::class.java)
        val loginVar  = apiManager.getUsers()
        loginVar.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserResponse> {
                    override fun onComplete() { }
                    override fun onSubscribe(d: Disposable) { }
                    override fun onNext(t: UserResponse) {
                        if (t.success)
                            sendView.getUsersOnSuccess(t.data)
                        else
                            sendView.getUsersOnFail(t.message)                    }
                    override fun onError(e: Throwable) {
                        sendView.getUsersOnFail(e.message.toString())
                    }
                })
    }
}