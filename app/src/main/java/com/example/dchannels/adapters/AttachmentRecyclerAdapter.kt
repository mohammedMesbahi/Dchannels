package com.example.dchannels.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dchannels.Constants
import com.example.dchannels.Models.Attachment
import com.example.dchannels.R
import com.example.dchannels.utilities.Utilities
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class AttachmentRecyclerAdapter(
    options: FirestoreRecyclerOptions<Attachment>,
    var context: Context
) :
    FirestoreRecyclerAdapter<Attachment, AttachmentRecyclerAdapter.AttachmentViewHolder>(options) {
    class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leftAttachmentLayout: View
        var leftSenderNameTv: TextView
        var leftSenderEmailTv: TextView
        var leftTextMessageTv: TextView
        var leftSenderProfileImage: ImageView
        var leftTextMessageTimeTv: TextView

        var rightAttachmentLayout: View
        var rightTextMessageTv: TextView
        var rightTextMessageTimeTv: TextView

        init {
            leftAttachmentLayout = itemView.findViewById(R.id.left_attachment_layout)
            leftSenderNameTv = itemView.findViewById(R.id.left_sender_name_tv)
            leftSenderEmailTv = itemView.findViewById(R.id.left_sender_email_tv)
            leftTextMessageTv = itemView.findViewById(R.id.left_text_message_tv)
            leftSenderProfileImage = itemView.findViewById(R.id.left_sender_profile_image)
            leftTextMessageTimeTv = itemView.findViewById(R.id.left_text_message_time_tv)

            rightAttachmentLayout = itemView.findViewById(R.id.right_attachment_layout)
            rightTextMessageTv = itemView.findViewById(R.id.right_text_message_tv)
            rightTextMessageTimeTv = itemView.findViewById(R.id.right_text_message_time_tv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val view = LayoutInflater.from(context)
        .inflate(R.layout.channel_attachment_recycler_row, parent, false)
        return AttachmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int, model: Attachment) {
        if (model.type.equals(Constants.ATTACHMENT_TYPE_TEXT)) {
            if (model.sender?.id.equals(FirebaseAuth.getInstance().currentUser?.uid)) {
                // if i am the sender
                holder.rightTextMessageTv.text = model.text
                holder.rightTextMessageTimeTv.text = Utilities.foramatDate(model.timestamp!!)
                holder.rightAttachmentLayout.visibility = View.VISIBLE
            } else {
                holder.leftSenderNameTv.text = model.sender?.name
                holder.leftSenderEmailTv.text = model.sender?.email
                holder.leftTextMessageTv.text = model.text
                holder.leftTextMessageTimeTv.text =
                    Utilities.foramatDate(model.timestamp!!)
                Log.d("AttachmentRecycler", "sender: ${model.sender?.profileImage}")
                Utilities.loadProfileImageIntoView(
                    holder.leftSenderProfileImage,
                    model.sender?.profileImage!!)
                holder.leftAttachmentLayout.visibility = View.VISIBLE
            }
        }
    }
}