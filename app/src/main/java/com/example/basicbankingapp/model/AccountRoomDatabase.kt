package com.example.basicbankingapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Account class
@Database(entities = arrayOf(Account::class, Transaction::class), version = 1, exportSchema = false)
abstract class AccountRoomDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AccountRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AccountRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if null, then create the database using elvis operator
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountRoomDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AccountDatabaseCallback(scope))
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class AccountDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.accountDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(accountDao: AccountDao) {
            // Delete all content.
            accountDao.deleteAll()

            // Add dummy accounts.
            accountDao.insert(Account("Yuuna Honoka", "789100001", "012-7495190", 1385.28, 1435.28))
            accountDao.insert(Account("Mohamad Ali", "789100002", "012-4552890", 4013.58, 4063.58))
            accountDao.insert(Account("Ryuu Saburō", "789100003", "016-3360355", 5373.99, 5423.99))
            accountDao.insert(Account("Seung Do-Yun", "789100004", "013-7959073 ", 6648.84, 6698.84))
            accountDao.insert(Account("Hina Yaeko", "789100005", "011-38120938", 6612.96, 6662.96))
            accountDao.insert(Account("Madison Frank", "789100006", "017-4631210", 9215.14, 9265.14))
            accountDao.insert(Account("Marty Effie", "789100007", "018-4331033", 5201.31, 5251.31))
            accountDao.insert(Account("Wen Ming Xu", "789100008", "010-7120880", 4017.37, 4067.37))
            accountDao.insert(Account("Cheng Bai Chong", "789100009", "013-1754980", 5294.31, 5344.31))
            accountDao.insert(Account("Muthu Sammy", "789100010", "016-6102990", 5636.18, 5686.18))


        }

    }
}
