package com.example.dchannels.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.example.dchannels.Models.User
import com.example.dchannels.R
import com.example.dchannels.doa.AdminDoaStore
import com.example.dchannels.doa.UserDoaStore
import com.example.dchannels.foa.FileUtilities
import com.example.dchannels.utilities.Utilities
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class UserRecyclerAdapter(options: FirestoreRecyclerOptions<User>, var context: Context) :
    FirestoreRecyclerAdapter<User, UserRecyclerAdapter.UserViewHolder>(options) {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_profile_image: ImageView
        var tv_name: TextView
        var tv_email: TextView
        var cb_isModerator: CheckBox

        init {
            iv_profile_image = itemView.findViewById(R.id.iv_profile_image)
            tv_name = itemView.findViewById(R.id.tv_name)
            tv_email = itemView.findViewById(R.id.tv_email)
            cb_isModerator = itemView.findViewById(R.id.cb_isModerator)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.user_recycler_row, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        holder.tv_name.text = model.name
        holder.tv_email.text = model.email
        holder.cb_isModerator.isChecked = (model.role == Constants.ROLE_MODERATOR)
        if (model.role == Constants.ROLE_MODERATOR || model.role == Constants.ROLE_USER) {
            holder.cb_isModerator.visibility = View.VISIBLE
        } else {
            holder.cb_isModerator.visibility = View.GONE
        }
        Glide.with(context)
            .load(model.profileImage)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.person_icon) // Placeholder image
            .into(holder.iv_profile_image)
    }

    companion object {
        fun getOptions(): FirestoreRecyclerOptions<User> {
            val query = UserDoaStore.getInstance().getAllUsersQuery()
            return FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User::class.java)
                .build()
        }
    }
}