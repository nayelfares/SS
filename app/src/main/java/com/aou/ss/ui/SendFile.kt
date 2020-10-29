package com.aou.ss.ui

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.aou.ss.R
import com.blakequ.rsa.FileEncryptionManager
import com.blakequ.rsa.FileUtils
import java.io.File
import java.io.FileInputStream

class NewFile : AppCompatActivity() {
    var mFileEncryptionManager: FileEncryptionManager? = null
    var publicKey = "zmZEKAEtOVb7hzlug8YuyMgmuQIZV39ogi2AntgqxW3qlDzQ9ivHAgViB79ZrJ6RTzb7VMc3KVHuJvtt1sGx5w=="
    var privateKey = "s35fr4a6e8r4fa84rf86va4er"
    var PICKFILE_RESULT_CODE = 1234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_file)
        mFileEncryptionManager = FileEncryptionManager.getInstance()
        val chooseFile = findViewById<AppCompatButton>(R.id.chooseFile)
    }

    fun select(v: View?) {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "*/*"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (true) {
            val uri = data!!.data
            val src = getPathFromUri(uri)
            try {
                mFileEncryptionManager!!.generateKey()
                privateKey = mFileEncryptionManager!!.privateKey
                publicKey = mFileEncryptionManager!!.publicKey
                val data1 = FileUtils.getBytesFromInputStream(FileInputStream(src))
                val file = File(Environment.getExternalStorageDirectory().toString() + "/folderName")
                if (!file.exists()) {
                    file.mkdirs()
                }
                val myappFile = File(file
                        .toString() + File.separator + "myapp.bin")
                val decFile = File(file
                        .toString() + File.separator + "1.jpg")
                mFileEncryptionManager!!.encryptFileByPublicKey(data1, myappFile)
                mFileEncryptionManager!!.decryptFileByPrivateKey(myappFile, decFile)
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                Log.e("error", e.message!!)
            }
        }
    }

    fun getPathFromUri(uri: Uri?): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                        split[1]
                )
                return getDataColumn(contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
                column
        )
        try {
            cursor = contentResolver.query(uri!!, projection, selection, selectionArgs,
                    null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri?): Boolean {
        return "com.android.externalstorage.documents" == uri!!.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri?): Boolean {
        return "com.android.providers.downloads.documents" == uri!!.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri?): Boolean {
        return "com.google.android.apps.photos.content" == uri!!.authority
    }

    companion object {
        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri?): Boolean {
            return "com.android.providers.media.documents" == uri!!.authority
        }
    }
}