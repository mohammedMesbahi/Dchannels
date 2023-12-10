package com.example.dchannels.foa

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class FileUtilities {
    companion object {
    private var instance:FileUtilities?=null
        fun getInstance():FileUtilities{
            if (instance==null){
                synchronized(FileUtilities::class){
                    if (instance==null){
                        instance= FileUtilities()
                    }
                }
            }
            return instance!!
        }
    }
    fun uploadFile(uri: Uri, path:String): UploadTask {
        val storageRef=FirebaseStorage.getInstance().reference
        val fileRef=storageRef.child(path)
        return fileRef.putFile(uri)
    }
    fun downloadFile(path:String): Task<Uri> {
        val storageRef=FirebaseStorage.getInstance().reference
        val fileRef=storageRef.child(path)
        return fileRef.downloadUrl
    }


}