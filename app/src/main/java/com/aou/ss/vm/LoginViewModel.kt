package com.aou.ss.vm

import android.content.Context
import com.aou.ss.api.MainAPIManager
import com.aou.ss.data.LoginResponse
import com.aou.ss.data.RequestInterface
import com.aou.ss.ui.LoginView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(val loginView: LoginView,val context: Context) {
    fun login(email:String,password:String){
        val apiManager= MainAPIManager().provideRetrofitInterface().create(RequestInterface::class.java)
        val loginVar  = apiManager.login(email,password)
        loginVar.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<LoginResponse> {
                    override fun onComplete() { }
                    override fun onSubscribe(d: Disposable) { }
                    override fun onNext(t: LoginResponse) {
                        if (t.success)
                            loginView.onSuccess(t.data.token)
                        else
                            loginView.onFailer(t.message)
                    }
                    override fun onError(e: Throwable) {
                        loginView.onFailer(e.message.toString())
                    }
                })
    }
}