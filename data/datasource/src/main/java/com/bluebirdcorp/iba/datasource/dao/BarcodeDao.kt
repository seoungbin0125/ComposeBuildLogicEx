package com.bluebirdcorp.iba.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bluebirdcorp.iba.data.model.BarcodeEntity

@Dao
interface BarcodeDao {
    @Query("SELECT * FROM Barcode ORDER BY id ASC")
    fun getAll(): List<BarcodeEntity>

    @Query("SELECT * FROM Barcode WHERE id = :id LIMIT 1")
    fun findById(id: Long): BarcodeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBarcode(barcode: BarcodeEntity)

    @Delete
    fun deleteBarcode(barcode: BarcodeEntity)
}