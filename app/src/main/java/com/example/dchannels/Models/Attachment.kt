package com.example.dchannels.Models

import com.google.firebase.Timestamp

open class Attachment(
    var id: String?=null,
    var sender:User?=null,
    var type:String?=null,
    var text: String?=null,
    var timestamp:Timestamp?=null
)