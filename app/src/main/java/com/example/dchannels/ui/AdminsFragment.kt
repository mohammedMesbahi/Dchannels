package com.example.dchannels.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dchannels.adapters.AdminRecyclerAdapter
import com.example.dchannels.databinding.FragmentAdminsBinding
import com.example.dchannels.doa.AdminDoaStore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminsFragment : Fragment() {
    private lateinit var adapter: AdminRecyclerAdapter
    private var _binding: FragmentAdminsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAdminsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.fabAddAdmin.setOnClickListener {
            showActivityAddAdmin()
        }
    }

    private fun setupRecyclerView() {
        adapter = AdminRecyclerAdapter(AdminRecyclerAdapter.getOptions(), requireContext())
        binding.adminsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.adminsRecyclerView.adapter = adapter
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

    private fun showActivityAddAdmin() {
        val intent = Intent(activity, AddAdminActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance(param1: String, param2: String) =
            AdminsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}