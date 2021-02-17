package com.example.basicbankingapp.model

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository) : ViewModel() {

    val allAccounts: LiveData<List<Account>> = repository.allAccounts.asLiveData()

    fun selectAccount(acc_number: String): LiveData<Account> {
        return repository.selectAccount(acc_number).asLiveData()
    }

    fun getAccount(acc_number: String): Account {
        return repository.getAccount(acc_number)
    }

    fun getAllRecipients(acc_number: String): LiveData<List<Account>> {
        return repository.getAllRecipients(acc_number).asLiveData()
    }


    /**
     * Wrapper method, implementation of insert() is encapsulated from the UI
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(account: Account) = viewModelScope.launch {
        repository.insert(account)
    }

    fun updateBalance(new_balance: Double, acc_number: String) = viewModelScope.launch {
        repository.updateBalance(new_balance, acc_number)
    }


    // For Transaction table
    val allTransactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()

    fun newTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.newTransaction(transaction)
    }

}

class AccountViewModelFactory(private val repository: AccountRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}