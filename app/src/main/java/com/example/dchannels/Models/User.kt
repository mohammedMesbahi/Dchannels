package com.example.dchannels.Models

open class User(
    var id: String?,
    var name: String?,
    var email: String?,
    var profileImage: String?,
    var role: String?
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
    fun signIn(email: String, password: String) {
        TODO("Not yet implemented")
    }
    fun signOut() {
        TODO("Not yet implemented")
    }
    fun sendAttachment(){
        TODO("Not yet implemented")

    }
}