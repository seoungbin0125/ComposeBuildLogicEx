package com.bluebirdcorp.softpos.feacture.payment.view_model

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.bluebirdcorp.softpos.barcode.const.TapxPhoneConstants
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.feacture.payment.MainActivity.Companion.TAG
import org.json.JSONException

class SharedPaymentViewModel : ViewModel() {

    private var activityLauncher: ActivityResultLauncher<Intent>? = null

    fun setActivityLauncher(launcher: ActivityResultLauncher<Intent>) {
        this.activityLauncher = launcher
    }

    fun requestTapxPhonePayment(context: Context, currecyType: String, amount: String) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage("by.iba.tapxphone")

        if (launchIntent != null) {
            debug("SharedPaymentViewModel", "Requesting Pay IBA app status")
            launchIntent.flags = 0
            try {
                launchIntent.putExtra("version", "1.0.1")
                launchIntent.putExtra("intent_type", "payment")
                launchIntent.putExtra("app_language_code", "en")
//                launchIntent.putExtra("merchant_bank_id", "3afabe98-27e9-46cb-9bcc-2d2a0fcc4b9d")
                launchIntent.putExtra("merchant_bank_id", "213b4089-d6da-11ef-959d-022df560c9d7")
                launchIntent.putExtra("solution_partner_id", "213b4089-d6da-11ef-959d-022df560c9d7")
                launchIntent.putExtra("device_setup_mode", 3)
                launchIntent.putExtra("trn_type", "100000001")
                launchIntent.putExtra("trn_currency", currecyType)
                launchIntent.putExtra("trn_amount", amount)
                launchIntent.putExtra("receipt_screen", false)
                launchIntent.putExtra("cashier_name", "seongbin.choi@bluebirdcorp.com")

                activityLauncher?.launch(launchIntent)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TapxPhoneConstants.MARKET_URL))
                context.startActivity(intent)
            } catch (anfe: ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TapxPhoneConstants.PLAY_STORE_URL))
                context.startActivity(intent)
            }
        }
    }

    fun getTapxPhoneStatus(
        context: Context,
        activityLauncher: ActivityResultLauncher<Intent>
    ) {
        val launchIntent =
            context.packageManager.getLaunchIntentForPackage("by.iba.tapxphone")

        if (launchIntent != null) {
            Log.d(TAG, "Requesting IBA app status")
            launchIntent.flags = 0
            try {
                launchIntent.putExtra("version", "1.0.1");
                launchIntent.putExtra("intent_type", "status");
                launchIntent.putExtra("app_language_code", "en");
                launchIntent.putExtra("merchant_bank_id", "7b72c89e-e51f-4d26-97f2-d1b138c4b173");
                launchIntent.putExtra("solution_partner_id", "213b4089-d6da-11ef-959d-022df560c9d7")

                activityLauncher.launch(launchIntent)
            } catch (e: JSONException) {
                Log.d(TAG, "error ! $e")
                e.printStackTrace()
            }
        } else {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TapxPhoneConstants.MARKET_URL))
                context.startActivity(intent)
            } catch (anfe: ActivityNotFoundException) {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(TapxPhoneConstants.PLAY_STORE_URL))
                context.startActivity(intent)
            }
        }
    }
}