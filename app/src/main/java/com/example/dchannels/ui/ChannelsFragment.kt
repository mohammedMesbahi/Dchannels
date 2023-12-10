package com.example.dchannels.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dchannels.Constants
import com.example.dchannels.Models.Channel
import com.example.dchannels.Models.User
import com.example.dchannels.adapters.ChannelRecyclerAdapter
import com.example.dchannels.databinding.DialogAddChannelBinding
import com.example.dchannels.databinding.FragmentChannelsBinding
import com.example.dchannels.doa.ChannelDoaStore
import com.example.dchannels.utilities.MyPreferences
import com.example.dchannels.utilities.Utilities
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import java.util.*

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
    private lateinit var adapter: ChannelRecyclerAdapter
    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPreferences: MyPreferences
    private lateinit var loggedInUser : User

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
        setupRecyclerView()
        myPreferences = MyPreferences(requireContext())
        loggedInUser = myPreferences.getUser()!!
        if (loggedInUser.role != Constants.ROLE_USER){
            binding.fabAddChannel.visibility = View.VISIBLE
            binding.fabAddChannel.setOnClickListener {
                showAddChannelDialog()
            }
        } else {
            binding.fabAddChannel.visibility = View.GONE
        }
    }

    private fun showAddChannelDialog() {
        // Create a BottomSheetDialog
        val dialog = BottomSheetDialog(requireContext())
        val bindingDialogAddChannel = DialogAddChannelBinding.inflate(layoutInflater)
        // Inflate and set the layout for the dialog
//        val view = layoutInflater.inflate(R.layout.dialog_add_channel, null)
        dialog.setContentView(bindingDialogAddChannel.root)
        bindingDialogAddChannel.btnAddChannel.setOnClickListener {
            Log.d("channel_registration", "start adding channel")

            val channelLabel = bindingDialogAddChannel.etChannelLabel.text.toString()
            val channelDescription = bindingDialogAddChannel.etChannelDescription.text.toString()
            if (channelLabel.isEmpty()) {
                Utilities.showToast(
                    requireContext() as AppCompatActivity,
                    "Channel label is required"
                )
                return@setOnClickListener
            }

            val channel = Channel()
            channel.label = channelLabel
            channel.description = channelDescription
            channel.isPublic = bindingDialogAddChannel.cbPublic.isChecked
//            channel.members = ArrayList()
//            channel.attachments = ArrayList()
            channel.timestamp = Timestamp(Date())
            ChannelDoaStore.getInstance().addChannel(channel).addOnCompleteListener {
                if (it.isSuccessful) {
                    Utilities.showToast(
                        requireContext() as AppCompatActivity,
                        "Channel added successfully"
                    )
                    channel.id = it.result?.id!!
                    ChannelDoaStore.getInstance().updateChannel(channel)
                    dialog.dismiss()
                } else {
                    Utilities.showToast(
                        requireContext() as AppCompatActivity,
                        "${it.exception?.message}"
                    )
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

    fun setupRecyclerView() {
        adapter = ChannelRecyclerAdapter(ChannelRecyclerAdapter.getOptions(), requireContext())
        binding.channelsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.channelsRecyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        if (adapter != null) adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) adapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null) adapter.notifyDataSetChanged()
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
