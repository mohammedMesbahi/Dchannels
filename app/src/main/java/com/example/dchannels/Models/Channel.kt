package com.example.dchannels.Models

data class Channel(
var id: String?=null,
var name: String?=null,
var description: String?=null,
var members: ArrayList<User>?=null,
var attachments: ArrayList<Attachment>?=null
)