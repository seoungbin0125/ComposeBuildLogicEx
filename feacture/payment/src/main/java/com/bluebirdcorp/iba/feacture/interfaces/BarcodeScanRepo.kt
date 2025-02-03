package com.bluebirdcorp.iba.feacture.interfaces

import kotlinx.coroutines.flow.SharedFlow

interface BarcodeScanRepo {
    fun getBarcodeScanFlow(): SharedFlow<Long>
    fun triggerBarcodeScan(barcodeId: Long)
}