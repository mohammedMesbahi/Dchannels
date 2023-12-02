package com.example.dchannels.Models

import com.example.dchannels.Constants

class Admin : User {
    var password: String

    constructor(name: String, email: String, password: String) : super(name, email){
        this.password = password
        super.role = Constants.ROLE_ADMIN
    }

    constructor(id: String,name: String, email: String, password: String) : super(name, email){
        super.id = id
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
    override fun toString(): String {
        return "Admin(id=$id, name=$name, email=$email, password=$password,profilImage=$profileImage, role=$role)"
    }
}