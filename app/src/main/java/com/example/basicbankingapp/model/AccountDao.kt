package com.example.basicbankingapp.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface AccountDao {
    // Account table
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllAccount(): Flow<List<Account>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $ACCOUNT_COL != :acc_number")
    fun getAllRecipients(acc_number: String): Flow<List<Account>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $ACCOUNT_COL = :acc_number")
    fun selectAccount(acc_number: String): Flow<Account>

    @Query("SELECT * FROM $TABLE_NAME WHERE $ACCOUNT_COL = :acc_number")
    fun getAccount(acc_number: String): Account

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account)

    @Query("UPDATE $TABLE_NAME SET $AVAILABLE_BAL_COL = :new_balance, $CURRENT_BAL_COL = :new_balance+50 WHERE $ACCOUNT_COL = :acc_number")
    suspend fun updateBalance(new_balance: Double, acc_number: String )

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()


    // Transaction table
    @Query("SELECT * FROM $TRANSACTION_TABLE_NAME")
    fun getAllTransaction(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun newTransaction(transaction: Transaction)
}