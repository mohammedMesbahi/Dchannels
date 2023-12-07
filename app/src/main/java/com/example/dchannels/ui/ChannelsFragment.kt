package com.example.dchannels.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dchannels.Constants
import com.example.dchannels.Models.Channel
import com.example.dchannels.databinding.DialogAddChannelBinding
import com.example.dchannels.databinding.FragmentChannelsBinding
import com.example.dchannels.utilities.Utilities
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChannelsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChannelsFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    val database = FirebaseDatabase.getInstance(Constants.REALTIME_DATABASE_URL)
    val channelsCollectionReffrence = database.getReference(Constants.CHANNELS_COLLECTION)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment with ViewBinding
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddChannel.setOnClickListener {
            showAddChannelDialog()
        }
        channelsCollectionReffrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<Channel>()
                Log.d("channel", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("channel_registration", "Failed to read value.", error.toException())
            }
        })
    }

    private fun showAddChannelDialog() {
        // Create a BottomSheetDialog
        val dialog = BottomSheetDialog(requireContext())
        val bindingDialogAddChannel = DialogAddChannelBinding.inflate(layoutInflater)
        // Inflate and set the layout for the dialog
//        val view = layoutInflater.inflate(R.layout.dialog_add_channel, null)
        dialog.setContentView(bindingDialogAddChannel.root)
        bindingDialogAddChannel.btnAddChannel.setOnClickListener  {
            Log.d("channel_registration", "start adding channel")

            val channelName = bindingDialogAddChannel.etChannelName.text.toString()
            val channelDescription = bindingDialogAddChannel.etChannelDescription.text.toString()
            if (channelName.isEmpty() || channelDescription.isEmpty()) {
                Utilities.showToast(requireContext() as AppCompatActivity, "Please fill all fields")
                return@setOnClickListener
            }
            val channel = Channel()
            val key = channelsCollectionReffrence.push().key
            if (key == null) {
                Log.w("key", "Couldn't get push key for channel")
                return@setOnClickListener
            }
            channel.id = key
            channel.name = channelName
            channel.description = channelDescription

            channelsCollectionReffrence.child(channel.id!!).setValue(channel).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("channel_registration", "channel added successfully ${it.result}")
                    Utilities.showToast(requireContext() as AppCompatActivity, "Channel added successfully")
                    dialog.dismiss()
                } else {
                    Log.d("channel_registration", "channel not added ${it.exception}")
                    Utilities.showToast(requireContext() as AppCompatActivity, "Channel not added ${it.exception?.message}")
                }
            }
        }
        // Optionally, you could also set the behavior for when the dialog is dismissed
        dialog.setOnDismissListener {
            // Handle the dismissal
        }
        // Show the dialog
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Use this factory method to create a new instance of this fragment
        fun newInstance(param1: String, param2: String): ChannelsFragment {
            return ChannelsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        }
    }
}
