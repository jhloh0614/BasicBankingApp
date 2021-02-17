package com.example.basicbankingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val DATABASE_NAME = "account_database"
const val TABLE_NAME = "account_table"
const val NAME_COL = "name"
const val PHONE_COL = "phone_number"
const val CURRENT_BAL_COL = "current_balance"
const val AVAILABLE_BAL_COL = "available_balance"
const val ACCOUNT_COL = "acc_number"


@Entity(tableName = TABLE_NAME)
data class Account(
    val name: String,
    @PrimaryKey val acc_number: String,
    val phone_number: String,
    var available_balance: Double,
    var current_balance: Double
) {
    override fun toString(): String {
        return "$NAME_COL: $name" +
                "\n$ACCOUNT_COL: $acc_number" +
                "\n$PHONE_COL: $phone_number" +
                "\n$AVAILABLE_BAL_COL: $available_balance" +
                "\n$CURRENT_BAL_COL: $current_balance" +
                "\n"
    }
}

