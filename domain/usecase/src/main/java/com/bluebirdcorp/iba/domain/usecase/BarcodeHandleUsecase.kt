package com.bluebirdcorp.iba.domain.usecase

import com.bluebirdcorp.iba.domain.interfaces.BarcodeHandelRepo
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