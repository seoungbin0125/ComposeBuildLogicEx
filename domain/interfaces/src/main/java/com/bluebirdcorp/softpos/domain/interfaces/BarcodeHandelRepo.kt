package com.bluebirdcorp.softpos.domain.interfaces

interface BarcodeHandelRepo {
    fun openBarcode()
    fun closeBarcode()
    fun setTriggerOn()
    fun setTriggerOff()
}