package com.bluebirdcorp.softpos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bluebirdcorp.softpos.domain.item.BarcodeItem

@Entity(tableName = "Barcode")
data class BarcodeEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val dollarPrice: Double,
    val euroPrice: Double
)

fun BarcodeItem.toEntity(): BarcodeEntity = BarcodeEntity(
    id = id,
    name = name,
    dollarPrice = dollarPrice,
    euroPrice = euroPrice,
)

fun BarcodeEntity.toDomain(): BarcodeItem = BarcodeItem(
    id = this.id,
    name = this.name,
    dollarPrice = this.dollarPrice,
    euroPrice = this.euroPrice
)

