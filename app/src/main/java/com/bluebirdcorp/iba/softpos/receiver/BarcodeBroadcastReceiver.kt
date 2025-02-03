package com.bluebirdcorp.iba.softpos.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bluebirdcorp.iba.common.utils.debug
import com.bluebirdcorp.iba.feacture.interfaces.BarcodeScanRepo
import dagger.hilt.android.AndroidEntryPoint
import java.io.UnsupportedEncodingException
import javax.inject.Inject

@AndroidEntryPoint
class BarcodeBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var barcodeScanRepo: BarcodeScanRepo

    val ACTION_BARCODE_CALLBACK_DECODING_DATA: String =
        "kr.co.bluebird.android.bbapi.action.BARCODE_CALLBACK_DECODING_DATA"

    val EXTRA_BARCODE_DECODING_DATA: String = "EXTRA_BARCODE_DECODING_DATA"

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        debug("onReceive! action : ${intent?.action}")
        if (action == ACTION_BARCODE_CALLBACK_DECODING_DATA) {
            val data: ByteArray? =
                intent.getByteArrayExtra(EXTRA_BARCODE_DECODING_DATA)

            data?.let {
                getBarcodeId(it)?.let {
                it1 -> barcodeScanRepo.triggerBarcodeScan(it1) } }
        }
    }

    fun getBarcodeId(data: ByteArray?): Long? {
        var dataResult = ""
        if (data != null) {
            dataResult = String(data)
            if (dataResult.contains("�")) {
                try {
                    dataResult = String(data, charset("Shift-JIS"))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
        }
        return dataResult.toLongOrNull()
    }
}