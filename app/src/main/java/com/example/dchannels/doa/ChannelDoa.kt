package com.example.dchannels.doa

import com.example.dchannels.Models.Channel
import com.google.android.gms.tasks.Task

interface ChannelDoa {
    fun updateChannel(channel: Channel): Task<Void>
}