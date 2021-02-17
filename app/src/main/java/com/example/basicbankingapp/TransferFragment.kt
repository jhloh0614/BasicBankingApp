package com.example.basicbankingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.basicbankingapp.adapter.TransferItemAdapter
import com.example.basicbankingapp.databinding.FragmentTransferBinding
import com.example.basicbankingapp.model.AccountViewModel
import com.example.basicbankingapp.model.AccountViewModelFactory

class TransferFragment : Fragment() {

    companion object {
        const val KEY = "account_number"
        const val KEY2 = "sender_available_balance"
    }

    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var senderAccountNumber: String
    private var senderAvailableBalance = 0.0
    private val accountViewModel: AccountViewModel by activityViewModels{
        AccountViewModelFactory((activity?.application as AccountsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            senderAccountNumber = it.getString(KEY).toString()
            senderAvailableBalance = it.getString(KEY2).toString().toDouble()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerView = transferRecyclerView
            lifecycleOwner = viewLifecycleOwner
        }

        val adapter = TransferItemAdapter(senderAccountNumber, senderAvailableBalance, accountViewModel)
        recyclerView.adapter = adapter
        accountViewModel.getAllRecipients(senderAccountNumber).observe(viewLifecycleOwner){ accounts ->
            accounts.let { adapter.submitList(it) }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}