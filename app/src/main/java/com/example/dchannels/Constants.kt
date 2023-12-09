package com.example.dchannels

object Constants {
    const val USERS_COLLECTION = "users"
    const val USER_ID_FIELD = "id"
    const val USER_NAME_FIELD = "userName"
    const val USER_EMAIL_FIELD = "email"
    const val USER_PASSWORD_FIELD = "password"
    const val USER_PROFILE_IMAGE_FIELD = "profileImage"
    const val USER_ROLE_FIELD = "role"

    const val ADMINS_COLLECTION = "admins"
    const val ROLE_ADMIN = "admin"
    const val ROLE_MODERATOR = "moderator"
    const val ROLE_USER = "user"

    const val KEY_IS_LOGGED_IN = "isLoggedIn"
    const val KEY_PREFERENCE_NAME = "chatAppPreference"

    const val FOLDER_PROFILE_PICS = "profile_pics"
    const val DEFAULT_PROFILE_IMAGE_PATH = "profile_pics/default_pic.png"

    const val FCM_TOKEN_FIELD = "fcmToken"
    const val default_web_client_id = "416756097229-fkinlkt07kfeov99dqmbvobiutorvp76.apps.googleusercontent.com"

    const val CHANNELS_COLLECTION = "channels"
    const val CHANNEL_ID_FIELD = "id"
    const val CHANNEL_LABEL_FIELD = "label"
    const val CHANNEL_DESCRIPTION_FIELD = "description"
    const val CHANNEL_MEMBERS_FIELD = "members"
    const val CHANNEL_ATTACHMENTS_FIELD = "attachments"
    const val CHANNEL_LAST_MESSAGE_FIELD = "lastMessage"
    const val CHANNEL_LAST_MESSAGE_TIMESTAMP_FIELD = "lastMessageTimestamp"
    const val CHANNEL_TIMESTAMP_FIELD = "timestamp"

    const val REALTIME_DATABASE_URL = "https://dchannels-backend-default-rtdb.europe-west1.firebasedatabase.app"


}