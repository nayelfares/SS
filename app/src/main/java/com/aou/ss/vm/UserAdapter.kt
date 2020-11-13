package com.aou.ss.vm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aou.ss.R
import com.aou.ss.api.MainAPIManager
import com.aou.ss.auth.AuthManager
import com.aou.ss.auth.showToast
import com.aou.ss.auth.toLink
import com.aou.ss.data.RequestInterface
import com.aou.ss.data.UploadResponse
import com.aou.ss.data.User
import com.bumptech.glide.Glide
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.user_item.view.*


class UserAdapter(val context:Context, val fileList:ArrayList<User>,val fileId:Long) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
        return view
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = fileList[position]

        Glide.with(context)
                .load(user.photo.toLink())
                .into(holder.profileImage)

        holder.userName.text=fileList[position].name

        holder.send.setOnClickListener {
            holder.send.isEnabled=false
            send(user.id,holder)
        }


    }

    // total number of rows
    override fun getItemCount(): Int {
        return fileList.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileImage =itemView.profileImage
        val userName=itemView.userName
        val send =itemView.send

    }


    fun send(userId:Long,holder: ViewHolder){
        val apiManager= MainAPIManager().provideRetrofitInterface().create(RequestInterface::class.java)
        val loginVar  = apiManager.send(AuthManager.token,userId,fileId)
        loginVar.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UploadResponse> {
                    override fun onComplete() { }
                    override fun onSubscribe(d: Disposable) { }
                    override fun onNext(t: UploadResponse) {
                        if (t.success){
                            holder.send.text="Sent"
                        }else{
                            showToast(context,t.message)
                            holder.send.isEnabled=true
                        }
                    }
                    override fun onError(e: Throwable) {
                        holder.send.isEnabled=true
                        showToast(context,e.message.toString())
                    }
                })
    }





}