package com.aou.ss.vm

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.aou.ss.MainActivity
import com.aou.ss.R
import com.aou.ss.api.MainAPIManager
import com.aou.ss.auth.FileUtils
import com.aou.ss.auth.showToast
import com.aou.ss.auth.toLink
import com.aou.ss.data.ProjectFile
import com.aou.ss.data.RequestInterface
import com.aou.ss.ui.SendActivity
import com.blakequ.rsa.FileEncryptionManager
import kotlinx.android.synthetic.main.file_item.view.*
import okhttp3.ResponseBody
import org.apache.commons.io.IOUtils
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


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
                holder.fileIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf))
            }
            "excel"->{
                holder.fileIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_excel))
            }
            "word"->{
                holder.fileIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_word))
            }
            "image"->{
                holder.fileIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_image))
            }
        }

        holder.fileIcon.setOnClickListener {
                (context as MainActivity).loading()
                download(animal.path.toLink(), position)
        }

        holder.send.setOnClickListener {

            val intent=Intent(context,SendActivity::class.java)
            intent.putExtra("fileId",animal.id)
            context.startActivity(intent)
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
        val send =itemView.send

    }


    fun download(link:String ,position: Int) {
        val apiManager = MainAPIManager().provideRetrofitInterface().create(RequestInterface::class.java)
        val loginVar = apiManager.downlload(link)
        loginVar.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                (context as MainActivity).stopLoading()
                val file = File(context.filesDir,"temp")
                try {

                    writeResponseBodyToDisk(response.body()!!,file.absolutePath)
                } catch (ex: Exception) {
                    showToast(context,ex.localizedMessage.toString())
                }

                val mFileEncryptionManager = FileEncryptionManager.getInstance();
                mFileEncryptionManager.setRSAKey(fileList[position].public_key,fileList[position].private_key,true)
                val decFile = File(context.filesDir, fileList[position].name)
                if (decFile.exists())
                    open(decFile.absolutePath)
                else {
                    doAsync {
                        mFileEncryptionManager!!.decryptFileByPrivateKey(file, decFile)
                        context.stopLoading()
                        open(decFile.absolutePath)
                    }
                }
            }
        })
    }

    fun open(filePath:String){
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(FileProvider.getUriForFile(context,"com.aou.ss.fileprovider",File(filePath)), FileUtils.getMimeType(filePath))
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showToast(context,"no available app")
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody,path:String): Boolean {
        return try {
            val futureStudioIconFile = File(path)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("TAG", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            false
        }
    }
}