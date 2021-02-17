package com.example.basicbankingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.basicbankingapp.R
import com.example.basicbankingapp.model.Transaction


class TransactionItemAdapter :
    ListAdapter<Transaction, TransactionItemAdapter.TransactionItemViewHolder>(ACCOUNT_COMPARATOR) {

    class TransactionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val senderTextView: TextView = view.findViewById(R.id.item_sender)
        val receiverTextView: TextView = view.findViewById(R.id.item_receiver)
        val dateTextView: TextView = view.findViewById(R.id.item_date)
        val transactAmountTextView: TextView = view.findViewById(R.id.item_transact_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_list_item, parent, false)
        return TransactionItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
        val currentAccount = getItem(position)
        holder.senderTextView.text = currentAccount.sender_name
        holder.receiverTextView.text = currentAccount.receiver_name
        holder.dateTextView.text = currentAccount.transact_date
        holder.transactAmountTextView.text =
            CustomersItemAdapter().formatBalance(currentAccount.transact_amount)

    }

    companion object {
        private val ACCOUNT_COMPARATOR = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}