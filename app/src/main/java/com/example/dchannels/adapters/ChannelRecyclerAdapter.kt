package com.example.dchannels.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dchannels.Models.Channel
import com.example.dchannels.R
import com.example.dchannels.doa.ChannelDoaStore
import com.example.dchannels.foa.FileUtilities
import com.example.dchannels.ui.ChannelActivity
import com.example.dchannels.utilities.Utilities
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChannelRecyclerAdapter(options: FirestoreRecyclerOptions<Channel>, var context: Context) :
    FirestoreRecyclerAdapter<Channel, ChannelRecyclerAdapter.ChannelViewHolder>(options) {
    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var channelLabel: TextView
        var channelImage: ImageView
        var tvChannelLastMessage: TextView

        init {
            channelLabel = itemView.findViewById(R.id.tv_channel_label)
            channelImage = itemView.findViewById(R.id.iv_channel_image)
            tvChannelLastMessage = itemView.findViewById(R.id.tv_channel_last_message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.channels_recycler_row, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int, model: Channel) {
        holder.channelLabel.text = model.label
        if (model.lastMessage != null) {
            holder.tvChannelLastMessage.text = model.lastMessage!!.text
        } else {
            holder.tvChannelLastMessage.text = ""
        }
        if (model.channelImage == null || model.channelImage!!.isEmpty()) {
            holder.channelImage.setImageResource(R.drawable.sharp_groups_24)
        } else {
            FileUtilities.getInstance().downloadFile(model.channelImage!!).addOnSuccessListener { uri ->
                Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.sharp_groups_24) // Placeholder image
                    .into(holder.channelImage)
            }.addOnFailureListener {
            }
        }
        holder.itemView.setOnClickListener {
            // navigate to channelActivity

            //navigate to chat activity
            val intent = Intent(context, ChannelActivity::class.java)
            Utilities.passChannelToIntent(intent, model)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    companion object {
        fun getOptions(): FirestoreRecyclerOptions<Channel> {
            val query = ChannelDoaStore.getInstance().getAllChannelsQuery()
            return FirestoreRecyclerOptions.Builder<Channel>()
                .setQuery(query, Channel::class.java).build()
        }
    }
}