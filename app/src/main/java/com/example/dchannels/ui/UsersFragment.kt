package com.example.dchannels.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dchannels.adapters.UserRecyclerAdapter
import com.example.dchannels.databinding.FragmentUsersBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UsersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = UserRecyclerAdapter(UserRecyclerAdapter.getOptions(), requireContext())
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.usersRecyclerView.adapter = adapter
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

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UsersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}