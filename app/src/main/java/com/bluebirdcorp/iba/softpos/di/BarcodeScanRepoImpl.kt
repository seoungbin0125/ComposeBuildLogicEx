package com.bluebirdcorp.iba.softpos.di

import com.bluebirdcorp.iba.common.utils.debug
import com.bluebirdcorp.iba.feacture.interfaces.BarcodeScanRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class BarcodeScanRepoImpl  @Inject constructor() : BarcodeScanRepo {
    private val _barcodeScanFlow = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    override fun getBarcodeScanFlow(): SharedFlow<Long> = _barcodeScanFlow

    override fun triggerBarcodeScan(barcodeId: Long) {
        debug("# triggerBarcodeScan !!")
        _barcodeScanFlow.tryEmit(barcodeId)
    }
}