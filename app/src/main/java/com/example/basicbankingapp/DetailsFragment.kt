package com.example.basicbankingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.basicbankingapp.adapter.CustomersItemAdapter
import com.example.basicbankingapp.databinding.DetailsFragmentBinding
import com.example.basicbankingapp.model.AccountViewModel
import com.example.basicbankingapp.model.AccountViewModelFactory

class DetailsFragment: Fragment() {

    companion object {
        const val KEY = "account_number"
        const val KEY2 = "sender_available_balance"
    }

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var senderAccountNumber: String
    private lateinit var senderAvailableBalance: String
    private val accountViewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory((activity?.application as AccountsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            senderAccountNumber = it.getString(KEY).toString()
            senderAvailableBalance = it.getString(KEY2).toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel.selectAccount(senderAccountNumber).observe(viewLifecycleOwner){
            binding.apply {
                nameDetails.text = it.name
                accountNumberDetails.text = it.acc_number
                phoneDetails.text = it.phone_number
                availableBalanceDetails.text = CustomersItemAdapter().formatBalance(it.available_balance)
                currentBalanceDetails.text = CustomersItemAdapter().formatBalance(it.current_balance)
            }
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            detailsFragment = this@DetailsFragment
            transferButton.setOnClickListener {
                goToTransferScreen()
            }
        }

    }

    private fun goToTransferScreen(){
        val action = DetailsFragmentDirections.actionDetailsFragmentToTransferFragment(senderAccountNumber, senderAvailableBalance)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}