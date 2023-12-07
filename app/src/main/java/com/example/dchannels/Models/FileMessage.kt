package com.example.dchannels.Models

class FileMessage(id: String?=null,var fileUrl: String?=null,sender:User?=null ) :Attachment(id,sender)