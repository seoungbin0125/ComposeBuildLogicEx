package com.bluebirdcorp.softpos.domain.usecase

import com.bluebirdcorp.softpos.domain.interfaces.BarcodeHandelRepo
import javax.inject.Inject

class BarcodeHandleUsecase @Inject constructor(
    private val barcodeHandleRepo: BarcodeHandelRepo
) {
    fun openBarcode() {
        barcodeHandleRepo.openBarcode()
    }

    fun closeBarcode() {
        barcodeHandleRepo.closeBarcode()
    }

    fun setTriggerOn() {
        barcodeHandleRepo.setTriggerOn()
    }

    fun setTriggerOff() {
        barcodeHandleRepo.setTriggerOff()
    }
}