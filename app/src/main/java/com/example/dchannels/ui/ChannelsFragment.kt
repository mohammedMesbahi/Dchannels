package com.example.dchannels.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dchannels.R
import com.example.dchannels.databinding.FragmentChannelsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

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
            Log.d("ChannelsFragment", "fabAddChannel clicked")
            showAddChannelDialog()
        }
    }

    private fun showAddChannelDialog() {
        // Create a BottomSheetDialog
        val dialog = BottomSheetDialog(requireContext())
        // Inflate and set the layout for the dialog
        val view = layoutInflater.inflate(R.layout.dialog_add_channel, null)
        dialog.setContentView(view)

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
