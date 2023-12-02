package com.example.dchannels.utilities

import android.content.Context
import com.example.dchannels.Constants

class MyPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("Identity", Context.MODE_PRIVATE)

    var id: String?
        get() = sharedPreferences.getString(Constants.USER_ID_FIELD, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_ID_FIELD, value).apply()
        }

    var name: String?
        get() = sharedPreferences.getString(Constants.USER_NAME_FIELD, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_NAME_FIELD, value).apply()
        }
    var email: String?
        get() = sharedPreferences.getString(Constants.USER_EMAIL_FIELD, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_EMAIL_FIELD, value).apply()
        }
    var profileImage:String
        get() = sharedPreferences.getString(Constants.USER_PROFILE_IMAGE_FIELD, Constants.DEFAULT_PROFILE_IMAGE_PATH).toString()
        set(value) {
            sharedPreferences.edit().putString(Constants.USER_PROFILE_IMAGE_FIELD, value).apply()
        }
}

// Usage
//val myPrefs = MyPreferences(context)
//val myValue = myPrefs.myStringValue // Read
//myPrefs.myStringValue = "new value" // Write
