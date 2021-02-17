package com.example.basicbankingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.basicbankingapp.adapter.CustomersItemAdapter
import com.example.basicbankingapp.databinding.FragmentCustomersBinding
import com.example.basicbankingapp.model.Account
import com.example.basicbankingapp.model.AccountViewModel
import com.example.basicbankingapp.model.AccountViewModelFactory

class CustomersFragment : Fragment() {

    private var _binding: FragmentCustomersBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val accountViewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory((activity?.application as AccountsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentCustomersBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            customersFragment = this@CustomersFragment
            recyclerView = customersRecyclerView
            lifecycleOwner = viewLifecycleOwner
        }


        val adapter = CustomersItemAdapter()
        recyclerView.adapter = adapter
        accountViewModel.allAccounts.observe(viewLifecycleOwner) { accounts ->
            // Update the cached copy of the accounts in the adapter.
            accounts.let { adapter.submitList(it) }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}