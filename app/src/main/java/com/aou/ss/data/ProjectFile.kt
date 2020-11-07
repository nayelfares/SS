package com.aou.ss.data

data class ProjectFile (val id:Long,val user_id:String,
                        val path:String,val name:String,
                        val type:String,val public_key:String,
                        val private_key:String,
                        val description:String
)

data class MyFilesResPonse (val success:Boolean,val message:String,val data:ArrayList<ProjectFile>)