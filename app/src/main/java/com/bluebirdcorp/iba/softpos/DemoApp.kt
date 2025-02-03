package com.bluebirdcorp.iba.softpos

import android.app.Application
import android.content.IntentFilter
import com.bluebirdcorp.iba.common.utils.debug
import com.bluebirdcorp.iba.softpos.receiver.BarcodeBroadcastReceiver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApp : Application() {

    private lateinit var barcodeReceiver: BarcodeBroadcastReceiver

    override fun onCreate() {
        super.onCreate()
        debug("onCreate")
        barcodeReceiver = BarcodeBroadcastReceiver()

        val filter = IntentFilter().apply {
            addAction(barcodeReceiver.ACTION_BARCODE_CALLBACK_DECODING_DATA)
        }
        registerReceiver(barcodeReceiver, filter)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(barcodeReceiver)
    }
}