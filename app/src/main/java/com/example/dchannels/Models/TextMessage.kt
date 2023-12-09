package com.example.dchannels.Models

import com.example.dchannels.Constants

class TextMessage(id: String?=null,sender:User?=null ) :Attachment(id,sender,
    Constants.ATTACHMENT_TYPE_TEXT)