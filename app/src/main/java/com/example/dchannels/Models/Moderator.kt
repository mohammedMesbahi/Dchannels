package com.example.dchannels.Models

import com.example.dchannels.Constants

class Moderator : User {
    constructor(
        name: String,
        email: String,
        profileImage: String
    ) : super(name,email,profileImage,Constants.ROLE_MODERATOR)
}