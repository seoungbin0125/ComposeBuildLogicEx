package com.bluebirdcorp.softpos.di

import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.feacture.interfaces.BarcodeScanRepo
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