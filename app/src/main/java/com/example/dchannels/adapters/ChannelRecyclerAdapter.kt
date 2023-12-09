package com.example.dchannels.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dchannels.Models.Channel
import com.example.dchannels.R
import com.example.dchannels.ui.ChannelActivity
import com.example.dchannels.utilities.Utilities
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChannelRecyclerAdapter(options: FirestoreRecyclerOptions<Channel>, var context: Context) :
    FirestoreRecyclerAdapter<Channel, ChannelRecyclerAdapter.ChannelViewHolder>(options) {
    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var channelLabel: TextView
        var channelImage: ImageView

        init {
            channelLabel = itemView.findViewById(R.id.tv_channel_label)
            channelImage = itemView.findViewById(R.id.iv_channel_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.channels_recycler_row, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int, model: Channel) {
        holder.channelLabel.text = model.label
        holder.channelImage.setImageResource(R.drawable.sharp_groups_24)
        holder.itemView.setOnClickListener {
            // navigate to channelActivity

            //navigate to chat activity
            val intent = Intent(context, ChannelActivity::class.java)
            Utilities.passChannelToIntent(intent, model)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}