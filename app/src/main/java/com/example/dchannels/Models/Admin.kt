package com.example.dchannels.Models

import com.example.dchannels.Constants

class Admin : User {
    var password: String

    constructor(name: String, email: String, password: String) : super(name, email){
        this.password = password
        super.role = Constants.ROLE_ADMIN
    }
    fun addModerator() {
        TODO()
    }

    fun removeModerator() {
        TODO()
    }

    fun addAdmin() {
        TODO()
    }

    fun removeAdmin() {
        TODO()
    }

    fun addChannel() {
        TODO()
    }

    fun removeChannel() {
        TODO()
    }
}