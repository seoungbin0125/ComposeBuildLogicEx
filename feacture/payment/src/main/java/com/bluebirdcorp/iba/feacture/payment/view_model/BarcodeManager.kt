package com.bluebirdcorp.iba.feacture.payment.view_model

import com.bluebirdcorp.iba.common.utils.debug
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarcodeManager @Inject constructor(
) {
    private val _barcodeValueFlow = MutableSharedFlow<Long>(extraBufferCapacity = 10, replay = 1)
    val barcodeValueFlow = _barcodeValueFlow.asSharedFlow()

    fun receiveBarcodeValue(value : Long) {
        debug("receiveBarcodeValue : $value")
        _barcodeValueFlow.tryEmit(value)
    }
}