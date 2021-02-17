package com.example.basicbankingapp.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because only need access to the DAO
class AccountRepository(private val accountDao: AccountDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allAccounts: Flow<List<Account>> = accountDao.getAllAccount()

    fun selectAccount(acc_number: String): Flow<Account>{
        return accountDao.selectAccount(acc_number)
    }

    fun getAllRecipients(acc_number: String): Flow<List<Account>>{
        return accountDao.getAllRecipients(acc_number)
    }

    fun getAccount(acc_number: String): Account{
        return accountDao.getAccount(acc_number)
    }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(account: Account){
        accountDao.insert(account)
    }

    @WorkerThread
    suspend fun updateBalance(new_balance: Double, acc_number: String ){
        accountDao.updateBalance(new_balance, acc_number)
    }


    // For Transaction table
    val allTransactions: Flow<List<Transaction>> = accountDao.getAllTransaction()

    @WorkerThread
    suspend fun newTransaction(transaction: Transaction){
        accountDao.newTransaction(transaction)
    }
}