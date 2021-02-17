package com.example.basicbankingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val TRANSACTION_TABLE_NAME = "transaction_table"
const val TRANSACT_SENDER_NAME_COL = "sender_name"
const val TRANSACT_SENDER_ACCOUNT_COL = "sender_account"
const val TRANSACT_RECEIVER_NAME_COL = "receiver_name"
const val TRANSACT_RECEIVER_ACCOUNT_COL = "receiver_account"
const val TRANSACT_AMOUNT_COL = "transact_amount"
const val TRANSACT_DATE_COL = "transact_date"

@Entity(tableName = TRANSACTION_TABLE_NAME)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val sender_name: String,
    val sender_account: String,
    val receiver_name: String,
    val receiver_account: String,
    val transact_amount: Double,
    val transact_date: String
)