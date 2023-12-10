package com.example.dchannels.Models

import com.example.dchannels.Constants

open class User(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var profileImage: String? = null,
    var role: String? = null
) {

/*    constructor(
        name: String,
        email: String,
        profileImage: String,
        role: String
    ) : this(name, email) {
        this.id = id
        this.profileImage = profileImage
        this.role = role
    }*/

    /*    constructor(name: String, email: String) {
            this.name = name
            this.email = email
        }*/
/*    constructor(){
        this.name = ""
        this.email = ""
        this.profileImage = ""
        this.role = ""
        this.id = ""
    }*/
    constructor() : this(null, null, null, null, null) {
        // Initialize default values if needed
    }

    fun signIn(email: String, password: String) {
        TODO("Not yet implemented")
    }

    fun signOut() {
        TODO("Not yet implemented")
    }

    fun sendAttachment() {
        TODO("Not yet implemented")

    }

    fun generateProfileImagePath(): String {
        if (id == null)
            throw Exception("User id is null")
        else
            return Constants.FOLDER_PROFILE_PICS + "/" + id
    }
}