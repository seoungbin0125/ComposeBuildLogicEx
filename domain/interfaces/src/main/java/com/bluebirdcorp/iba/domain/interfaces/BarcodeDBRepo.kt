package com.bluebirdcorp.iba.domain.interfaces

interface BarcodeDBRepo <T> {
    suspend fun deleteBarcodeItem()
    suspend fun findItemByBarcodeId(barcodeId: Long): T?
    suspend fun insertBarcodeItem(barcodeItem: T)
    suspend fun getBarcodeItems(): List<T>
}