package com.example.dchannels.doa

import com.example.dchannels.Models.Admin
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query

interface AdminDoa {
    fun addAdmin(admin: Admin): Task<Void>
    fun getAllAdmins():List<Admin>
    fun updateAdminImage(admin: Admin): Any
    fun getAdminById(id: String):Task<DataSnapshot>
}