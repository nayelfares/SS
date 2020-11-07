package com.aou.ss.vm

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aou.ss.R
import com.aou.ss.api.MainAPIManager
import com.aou.ss.auth.FileUtils
import com.aou.ss.auth.showToast
import com.aou.ss.auth.toLink
import com.aou.ss.data.ProjectFile
import com.aou.ss.data.RequestInterface
import com.blakequ.rsa.FileEncryptionManager
import kotlinx.android.synthetic.main.file_item.view.*
import okhttp3.ResponseBody
import org.apache.commons.io.IOUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class FileAdapter(val context:Context,val fileList:ArrayList<ProjectFile>) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false))
        return view
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = fileList[position]
        holder.fileName.text = animal.name
        holder.description.text=animal.description
        when(animal.type){
            "pdf"->{
                holder.fileIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_pdf))
            }
            "excel"->{
                holder.fileIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_excel))
            }
            "word"->{
                holder.fileIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_word))
            }
            "image"->{
                holder.fileIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_image))
            }
        }

        holder.fileIcon.setOnClickListener {
            download(animal.path.toLink(),position)


        }
    }

    // total number of rows
    override fun getItemCount(): Int {
        return fileList.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var fileName: TextView=itemView.fileName
        val fileIcon=itemView.typeIcon
        val description =itemView.description

    }


    fun download(link:String ,position: Int) {
        val apiManager = MainAPIManager().provideRetrofitInterface().create(RequestInterface::class.java)
        val loginVar = apiManager.downlload(link)
        loginVar.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                showToast(context,"Downloadd")
                val file = File(context.filesDir,"temp")
                try {

                    val fileOutputStream = FileOutputStream(file)
                    IOUtils.write(response.body()!!.bytes(), fileOutputStream)
                } catch (ex: Exception) {
                }

                val mFileEncryptionManager = FileEncryptionManager.getInstance();
                mFileEncryptionManager.setRSAKey(fileList[position].public_key,fileList[position].private_key,true)
                val decFile = File(context.filesDir, fileList[position].name)
                mFileEncryptionManager!!.decryptFileByPrivateKey(file, decFile)
                open(decFile.absolutePath)
            }
        })
    }

    fun open(filePath:String){
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(filePath), FileUtils.getMimeType(filePath))
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showToast(context,"no available app")
        }
    }
}