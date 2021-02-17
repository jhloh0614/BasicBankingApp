package com.example.basicbankingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.basicbankingapp.adapter.TransactionItemAdapter
import com.example.basicbankingapp.databinding.FragmentTransactionBinding
import com.example.basicbankingapp.model.AccountViewModel
import com.example.basicbankingapp.model.AccountViewModelFactory

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
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
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.transactionRecyclerView

        val adapter = TransactionItemAdapter()
        recyclerView.adapter = adapter
        accountViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            transactions.let { adapter.submitList(it) }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}