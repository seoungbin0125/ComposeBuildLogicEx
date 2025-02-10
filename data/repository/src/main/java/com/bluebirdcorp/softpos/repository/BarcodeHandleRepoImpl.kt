package com.bluebirdcorp.softpos.repository

import android.content.Context
import android.content.Intent
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.domain.interfaces.BarcodeHandelRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarcodeHandleRepoImpl @Inject constructor(
    @ApplicationContext private val mContext: Context,
) : BarcodeHandelRepo {
    private var mCurrentStatus: String? = null

    val ACTION_BARCODE_OPEN: String = "kr.co.bluebird.android.bbapi.action.BARCODE_OPEN"
    val ACTION_BARCODE_CLOSE: String = "kr.co.bluebird.android.bbapi.action.BARCODE_CLOSE"
    val EXTRA_INT_DATA3: String = "EXTRA_INT_DATA3"

    private val STATUS_CLOSE: String = "STATUS_CLOSE"
    private val STATUS_OPEN: String = "STATUS_OPEN"
    private val STATUS_TRIGGER_ON: String = "STATUS_TRIGGER_ON"
    private val SEQ_BARCODE_OPEN: Int = 100
    private val SEQ_BARCODE_CLOSE: Int = 200
    private val SEQ_BARCODE_GET_STATUS: Int = 300
    private val SEQ_BARCODE_SET_TRIGGER_ON: Int = 400
    private val SEQ_BARCODE_SET_TRIGGER_OFF: Int = 500
    private val SEQ_BARCODE_SET_PARAMETER: Int = 600
    private val SEQ_BARCODE_GET_PARAMETER: Int = 700


    override fun openBarcode() {
        if (mCurrentStatus == STATUS_OPEN) return
        val intent = Intent(ACTION_BARCODE_OPEN)
        intent.putExtra(EXTRA_INT_DATA3, SEQ_BARCODE_OPEN)
        mContext.sendBroadcast(intent)
    }

    override fun closeBarcode() {
        if (mCurrentStatus == STATUS_CLOSE) return
        val intent = Intent(ACTION_BARCODE_CLOSE)
        intent.putExtra(EXTRA_INT_DATA3, SEQ_BARCODE_CLOSE)
        mContext.sendBroadcast(intent)
    }

    override fun setTriggerOn() {
        debug("setTriggerOn")
        val intent = Intent()
        intent.setAction("kr.co.bluebird.android.bbapi.action.BARCODE_SET_TRIGGER")
        intent.putExtra("EXTRA_INT_DATA2", 1)
        intent.putExtra("EXTRA_INT_DATA3", SEQ_BARCODE_SET_TRIGGER_ON)
        mContext.sendBroadcast(intent)
    }

    override fun setTriggerOff() {
        debug("setTriggerOff")
        val intent = Intent()
        intent.setAction("kr.co.bluebird.android.bbapi.action.BARCODE_SET_TRIGGER")
        intent.putExtra("EXTRA_INT_DATA2", 0)
        intent.putExtra("EXTRA_INT_DATA3", SEQ_BARCODE_SET_TRIGGER_OFF)
        mContext.sendBroadcast(intent)
    }
}