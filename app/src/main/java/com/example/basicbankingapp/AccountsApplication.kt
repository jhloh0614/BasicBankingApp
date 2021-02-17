package com.example.basicbankingapp

import android.app.Application
import com.example.basicbankingapp.model.AccountRepository
import com.example.basicbankingapp.model.AccountRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AccountsApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AccountRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { AccountRepository(database.accountDao()) }
}