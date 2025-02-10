package com.bluebirdcorp.softpos.domain.item

data class BarcodeItem(
    val id: Long,
    val name: String,
    val dollarPrice: Double,
    val euroPrice: Double,
)


fun BarcodeItem.toDomain(): BarcodeItem = BarcodeItem(
    id = id,
    name = name,
    dollarPrice = dollarPrice,
    euroPrice = euroPrice,
)
