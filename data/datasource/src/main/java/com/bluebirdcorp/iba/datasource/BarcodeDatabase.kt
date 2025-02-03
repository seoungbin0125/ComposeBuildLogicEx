package com.bluebirdcorp.iba.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bluebirdcorp.iba.data.model.BarcodeEntity
import com.bluebirdcorp.iba.datasource.dao.BarcodeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [BarcodeEntity::class], version = 1)
abstract class BarcodeDatabase : RoomDatabase() {
    abstract fun barcodeDao(): BarcodeDao

    companion object {
        @Volatile
        private var INSTANCE: BarcodeDatabase? = null

        fun getDatabase(context: Context): BarcodeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarcodeDatabase::class.java,
                    "barcode_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val barcodeDao = INSTANCE?.barcodeDao()
                                barcodeDao?.insertBarcode(BarcodeEntity(id = 8801019009082, name = "Jelly Gems", dollarPrice = 5.0, euroPrice = 5.0))
                                barcodeDao?.insertBarcode(BarcodeEntity(id = 8801117361105, name = "Giant Wiggly Worms", dollarPrice = 2.0, euroPrice = 2.0))
                                barcodeDao?.insertBarcode(BarcodeEntity(id = 8801117363307, name = "Sour Blast Chews", dollarPrice = 3.0, euroPrice = 3.0))
                                barcodeDao?.insertBarcode(BarcodeEntity(id = 8801117362409, name = "Plum Bites", dollarPrice = 3.0, euroPrice = 3.0))
                                barcodeDao?.insertBarcode(BarcodeEntity(id = 6926106106092, name = "Super Sour Candy", dollarPrice = 4.0, euroPrice = 4.0))
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}