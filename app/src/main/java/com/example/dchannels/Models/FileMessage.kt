package com.example.dchannels.Models

import com.example.dchannels.Constants

class FileMessage(id: String?=null,var fileUrl: String?=null,sender:User?=null ) :Attachment(id,sender,Constants.ATTACHMENT_TYPE_FILE)