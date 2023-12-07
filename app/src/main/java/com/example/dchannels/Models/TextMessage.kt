package com.example.dchannels.Models

class TextMessage(id: String?=null,var text: String?=null,sender:User?=null ) :Attachment(id,sender)