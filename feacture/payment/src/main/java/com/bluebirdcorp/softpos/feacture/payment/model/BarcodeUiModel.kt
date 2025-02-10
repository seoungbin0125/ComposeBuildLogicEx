package com.bluebirdcorp.softpos.feacture.payment.model

import com.bluebirdcorp.softpos.domain.item.BarcodeItem

data class BarcodeUiModel(
    val id: Long,
    val name: String,
    val dollarPrice: Double,
    val euroPrice: Double,
    var index: Int = -1,
)

fun BarcodeUiModel.toDomain(): BarcodeItem = BarcodeItem(
    id = id,
    name = name,
    dollarPrice = dollarPrice,
    euroPrice = euroPrice,
)

fun BarcodeItem.toUi(): BarcodeUiModel = BarcodeUiModel(
    id = id,
    name = name,
    dollarPrice = dollarPrice,
    euroPrice = euroPrice,
)
