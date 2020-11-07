package com.aou.ss.vm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aou.ss.R
import com.aou.ss.data.ProjectFile
import com.blakequ.rsa.FileEncryptionManager
import kotlinx.android.synthetic.main.file_item.view.*


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
            val mFileEncryptionManager = FileEncryptionManager.getInstance();
            mFileEncryptionManager.setRSAKey(animal.public_key,animal.private_key,false)

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

}