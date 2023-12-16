package com.example.dchannels.Models

import com.example.dchannels.Constants
import com.google.firebase.Timestamp

data class Channel(
    var id: String?=null,
    var label: String?=null,
    var description: String?=null,
    var isPublic:Boolean?=null,
//    var members: ArrayList<User>?=null,
//    var attachments: ArrayList<Attachment>?=null,
    var lastMessage: Attachment?=null,
    var timestamp: Timestamp?=null,
    var lastMessageTimestamp: Timestamp?=null,
    var channelImage: String?=null
){
    fun generateChannelImagePath(): String {
        if (id == null)
            throw Exception("channel id is null")
        else
            return Constants.FOLDER_CHANNEL_PICS + "/" + id
    }
}