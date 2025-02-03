package com.bluebirdcorp.iba.domain.interfaces

interface BarcodeHandelRepo {
    fun openBarcode()
    fun closeBarcode()
    fun setTriggerOn()
    fun setTriggerOff()
}