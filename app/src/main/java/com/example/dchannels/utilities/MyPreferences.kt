package com.example.dchannels.utilities

import android.content.Context
import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.example.dchannels.Models.User
import com.google.firebase.auth.FirebaseUser

class MyPreferences(context: Context) {
    fun setAdmin(admin: Admin) {
        id = admin.id!!
        name = admin.name!!
        email = admin.email!!
        profileImage = admin.profileImage!!
        role = Constants.ROLE_ADMIN
    }
    fun getAdmin(): Admin {
        return Admin(id,name,email,profileImage)
    }


    fun setUser(user: FirebaseUser?) {
        id = user?.uid!!
        name = user.displayName!!
        email = user.email!!
        profileImage = user.photoUrl.toString()
        role = Constants.ROLE_USER
    }

    fun getUser(): User {
        return User(id,name,email,profileImage,role)
    }

    private val sharedPreferences = context.getSharedPreferences("Identity", Context.MODE_PRIVATE)

    var id: String
        get() = sharedPreferences.getString(Constants.USER_ID_FIELD, "")!!
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_ID_FIELD, value).apply()
        }

    var name: String
        get() = sharedPreferences.getString(Constants.USER_NAME_FIELD, "")!!
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_NAME_FIELD, value).apply()
        }
    var email: String
        get() = sharedPreferences.getString(Constants.USER_EMAIL_FIELD, "")!!
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_EMAIL_FIELD, value).apply()
        }
    var profileImage:String
        get() = sharedPreferences.getString(Constants.USER_PROFILE_IMAGE_FIELD, "")!!
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_PROFILE_IMAGE_FIELD, value).apply()
        }
    var role:String
        get() = sharedPreferences.getString(Constants.USER_ROLE_FIELD, "")!!
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_ROLE_FIELD, value).apply()
        }

}

// Usage
//val myPrefs = MyPreferences(context)
//val myValue = myPrefs.myStringValue // Read
//myPrefs.myStringValue = "new value" // Write
