package com.example.dchannels.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dchannels.Models.Channel
import com.example.dchannels.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChannelsRecyclerAdapter(options: FirestoreRecyclerOptions<Channel>,var context: Context) :
    FirestoreRecyclerAdapter<Channel, ChannelsRecyclerAdapter.ChannelViewHolder>(options) {
    class ChannelViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
         var channelLabel:TextView
         var channelImage: ImageView
         init {
                channelLabel = itemView.findViewById(R.id.tv_channel_label)
                channelImage = itemView.findViewById(R.id.iv_channel_image)
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.channels_recycler_row,parent,false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int, model: Channel) {
        holder.channelLabel.text = model.label
        holder.channelImage.setImageResource(R.drawable.sharp_groups_24)
    }
}