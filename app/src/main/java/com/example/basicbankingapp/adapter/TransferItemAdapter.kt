package com.example.basicbankingapp.adapter


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.basicbankingapp.R
import com.example.basicbankingapp.TransferFragmentDirections
import com.example.basicbankingapp.model.Account
import com.example.basicbankingapp.model.AccountViewModel
import com.example.basicbankingapp.model.Transaction
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*


class TransferItemAdapter(
    private val sender: String,
    private var senderAvailableBalance: Double,
    private val accountViewModel: AccountViewModel,
) :
    ListAdapter<Account, TransferItemAdapter.TransferItemViewHolder>(ACCOUNT_COMPARATOR) {

    private var transferAmount: Double = 0.0
    private lateinit var amountTextField: TextInputLayout
    private lateinit var amountInputEditText: TextInputEditText

    class TransferItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.item_name)
        val phoneTextView: TextView = view.findViewById(R.id.item_phone)
        val accountCardView: MaterialCardView = view.findViewById(R.id.account_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransferItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.customers_list_item, parent, false)
        return TransferItemViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: TransferItemViewHolder, position: Int) {
        val currentAccount = getItem(position)
        holder.nameTextView.text = currentAccount.name
        holder.phoneTextView.text = currentAccount.phone_number

        val context = holder.view.context
        val receiver = currentAccount.acc_number
        val receiverAvailableBalance = currentAccount.available_balance
        holder.accountCardView.setOnClickListener {
            showTransferDialog(context, holder, receiver, receiverAvailableBalance)
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


    private fun showTransferDialog(
        context: Context,
        holder: TransferItemViewHolder,
        receiver: String,
        receiverAvailableBalance: Double
    ) {

        val builder = MaterialAlertDialogBuilder(context)

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_view, null)
        amountTextField = view.findViewById(R.id.textField)
        amountInputEditText = view.findViewById(R.id.text_input_edit_text)
        amountTextField.prefixText = "RM"
        amountInputEditText.addDecimalLimiter()
        layout.addView(view)

        builder.setTitle(R.string.dialog_title)
            .setView(layout)
            .setPositiveButton("Transfer") { _, _ -> }
            .setNegativeButton("Cancel") { _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (isTransferable()) {
                transferMoney(receiver, receiverAvailableBalance)
                Toast.makeText(context, "Transfer Success!", Toast.LENGTH_LONG).show()
                val action = TransferFragmentDirections.actionTransferFragmentToNavigationCustomer()
                holder.view.findNavController().navigate(action)
                dialog.dismiss()
            }

        }


    }

    private fun setErrorTextField(error: Boolean, message: String) {
        if (error) {
            amountTextField.isErrorEnabled = true
            amountTextField.error = message
        } else {
            amountTextField.isErrorEnabled = false
            amountInputEditText.text = null
        }
    }

    private fun transferMoney(receiver: String, receiverAvailableBalance: Double) {
        accountViewModel.updateBalance(receiverAvailableBalance + transferAmount, receiver)
        accountViewModel.updateBalance(senderAvailableBalance - transferAmount, sender)
        val senderAccount = accountViewModel.getAccount(sender)
        val receiverAccount = accountViewModel.getAccount(receiver)
        val senderName = senderAccount.name
        val receiverName = receiverAccount.name

        val myLocale = Locale("en", "MY")
        val formatter = SimpleDateFormat("d/MMM/yyyy, HH:mm:ss", myLocale)
        val calendar = Calendar.getInstance()
        val date = formatter.format(calendar.time)

        val transaction = Transaction(0, senderName, sender, receiverName, receiver, transferAmount, date)
        accountViewModel.newTransaction(transaction)
    }

    private fun isTransferable(): Boolean {
        senderAvailableBalance = String.format("%.2f", senderAvailableBalance).toDouble()
        Log.d("Test", senderAvailableBalance.toString())
        if (!amountInputEditText.text.isNullOrBlank()) {
            transferAmount = amountInputEditText.text.toString().toDouble()
            if (senderAvailableBalance < transferAmount) {
                setErrorTextField(true, "Insufficient balance!")
            } else if (transferAmount == 0.0) {
                setErrorTextField(true, "Please enter an amount greater than RM0.00")
            } else {
                setErrorTextField(false, "")
                return true
            }
        } else {
            setErrorTextField(true, "Please enter an amount")
        }
        return false

    }

    private fun EditText.addDecimalLimiter(maxLimit: Int = 2) {

        this.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val str = this@addDecimalLimiter.text!!.toString()
                if (str.isEmpty()) return
                val str2 = decimalLimiter(str, maxLimit)

                if (str2 != str) {
                    this@addDecimalLimiter.setText(str2)
                    val pos = this@addDecimalLimiter.text!!.length
                    this@addDecimalLimiter.setSelection(pos)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    private fun EditText.decimalLimiter(string: String, MAX_DECIMAL: Int): String {

        var str = string
        if (str[0] == '.') str = "0$str"
        val max = str.length

        var rFinal = ""
        var after = false
        var i = 0
        var up = 0
        var decimal = 0
        var t: Char

        val decimalCount = str.count { ".".contains(it) }

        if (decimalCount > 1)
            return str.dropLast(1)

        while (i < max) {
            t = str[i]
            if (t != '.' && !after) {
                up++
            } else if (t == '.') {
                after = true
            } else {
                decimal++
                if (decimal > MAX_DECIMAL)
                    return rFinal
            }
            rFinal += t
            i++
        }
        return rFinal
    }

}