package com.example.dchannels.Models

import com.example.dchannels.Constants
import com.example.dchannels.doa.AdminDoaStore
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class Admin(
    id: String?=null,
    name: String?=null,
    email: String?=null,
    profileImage: String?=null,
    var password: String?=null
) : User(id,name,email,profileImage,Constants.ROLE_ADMIN) {
/*
    constructor(name: String, email: String, password: String) : super(name, email){
        this.password = password
        super.role = Constants.ROLE_ADMIN
    }*/

/*    constructor(id: String,name: String, email: String, password: String) : super(name, email){
        super.id = id
        this.password = password
        super.role = Constants.ROLE_ADMIN
    }*/
/*
    constructor(
        id: String,
        name: String,
        email: String,
        profileImage: String,
        password: String
    ) : super(name, email, profileImage,Constants.ROLE_ADMIN) {
        super.id = id
        this.password = password
    }*/

    override fun toString(): String {
        return "Admin(id=$id, name=$name, email=$email, password=$password,profilImage=$profileImage, role=$role)"
    }

     fun addAdmin(newAdmin: Admin): Task<Void> {
         return AdminDoaStore.getInstance().addAdmin(newAdmin)
     }

    fun createAccountForAdmin(newAdmin: Admin): Task<AuthResult> {
        return AdminDoaStore.getInstance().createAccountForAdmin(newAdmin)
    }
}