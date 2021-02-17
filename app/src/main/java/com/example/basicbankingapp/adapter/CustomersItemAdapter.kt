package com.example.basicbankingapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.basicbankingapp.CustomersFragmentDirections
import com.example.basicbankingapp.R
import com.example.basicbankingapp.model.Account
import com.google.android.material.card.MaterialCardView
import java.text.NumberFormat
import java.util.*

class CustomersItemAdapter() :
    ListAdapter<Account, CustomersItemAdapter.CustomersItemViewHolder>(ACCOUNT_COMPARATOR) {

    class CustomersItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.item_name)
        val phoneTextView: TextView = view.findViewById(R.id.item_phone)
        val currentBalanceTextView: TextView = view.findViewById(R.id.item_current_balance)
        val accountCardView: MaterialCardView = view.findViewById(R.id.account_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomersItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.customers_list_item, parent, false)
        return CustomersItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CustomersItemViewHolder, position: Int) {
        val currentAccount = getItem(position)
        holder.nameTextView.text = currentAccount.name
        holder.phoneTextView.text = currentAccount.phone_number
        holder.currentBalanceTextView.text = formatBalance(currentAccount.current_balance)
        val accountNumber = currentAccount.acc_number
        val availableBalance = currentAccount.available_balance.toString()
        holder.accountCardView.setOnClickListener {
            val action =
                CustomersFragmentDirections.actionNavigationCustomerToDetailsFragment(accountNumber, availableBalance)
            holder.view.findNavController().navigate(action)
        }
    }

    companion object {
        private val ACCOUNT_COMPARATOR = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
                return oldItem.acc_number == newItem.acc_number
            }
        }
    }

    fun formatBalance(balance: Double): String {
        val myLocale = Locale("en", "MY")
        val formattedBalance = NumberFormat.getCurrencyInstance(myLocale).format(balance)
        return formattedBalance
    }
}