package com.example.dchannels.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dchannels.Models.Admin
import com.example.dchannels.R
import com.example.dchannels.doa.AdminDoaStore
import com.example.dchannels.utilities.Utilities
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AdminRecyclerAdapter(options: FirestoreRecyclerOptions<Admin>, var context: Context) :
    FirestoreRecyclerAdapter<Admin, AdminRecyclerAdapter.AdminViewHolder>(options) {
    class AdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_profile_image: ImageView
        var tv_name: TextView
        var tv_email: TextView

        init {
            iv_profile_image = itemView.findViewById(R.id.iv_profile_image)
            tv_name = itemView.findViewById(R.id.tv_name)
            tv_email = itemView.findViewById(R.id.tv_email)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.admin_recycler_row, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int, model: Admin) {
        holder.tv_name.text = model.name
        holder.tv_email.text = model.email
        Utilities.loadProfileImageIntoView(holder.iv_profile_image, model.profileImage!!)
    }

    companion object {
        fun getOptions(): FirestoreRecyclerOptions<Admin> {
            val query = AdminDoaStore.getInstance().getAllAdminsQuery()
            return FirestoreRecyclerOptions.Builder<Admin>()
                .setQuery(query, Admin::class.java)
                .build()
        }
    }
}