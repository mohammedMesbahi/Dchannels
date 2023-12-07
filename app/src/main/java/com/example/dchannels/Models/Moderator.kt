package com.example.dchannels.Models

import com.example.dchannels.Constants

class Moderator(
    id: String?=null,
    name: String?=null,
    email: String?=null,
    profileImage: String?=null,
) : User(id,name,email,profileImage,Constants.ROLE_MODERATOR)