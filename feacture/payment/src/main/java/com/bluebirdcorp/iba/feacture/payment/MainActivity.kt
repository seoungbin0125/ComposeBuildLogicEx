package com.bluebirdcorp.iba.feacture.payment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import com.bluebirdcorp.iba.barcode.const.TapxPhoneConstants
import org.json.JSONException

class MainActivity : ComponentActivity() {
    companion object {
        val TAG = this::class.java.simpleName
    }

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "result.resultCode : ${result.resultCode}")
            result.data
            when (result.resultCode) {
                RESULT_OK -> {
                    val data = result.data
                    val serverMessage = data?.getStringExtra("server_message") ?: "No server message"
                    val intent_result = data?.getStringExtra("intent_result") ?: "No intent_result"
                    Log.d(TAG, "Transaction completed successfully. Server Message: $serverMessage")
                    Log.d(TAG, "Transaction completed successfully. intent_result: $intent_result")
                }
                RESULT_CANCELED -> {
                    val data = result.data
                    val serverMessage = data?.getStringExtra("server_message") ?: "No server message"
                    Log.d(TAG, "Transaction was canceled by the user. Server Message: $serverMessage")
                }
                else -> {
                    val data = result.data
                    val serverMessage = data?.getStringExtra("server_message") ?: "No server message"
                    Log.d(TAG, "Unexpected result code: ${result.resultCode}. Server Message: $serverMessage")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TapxPhoneConstants.PLAY_STORE_URL))
                context.startActivity(intent)
            }
        }
    }

    fun requestTapxPhonePayment(
        context: Context,
        activityLauncher: ActivityResultLauncher<Intent>
    ) {
        val launchIntent =
            context.packageManager.getLaunchIntentForPackage("by.iba.tapxphone")

        if (launchIntent != null) {
            Log.d(TAG, "Requesting Pay IBA app status")
            launchIntent.flags = 0
            try {
                launchIntent.putExtra("version", "1.0.1");
                launchIntent.putExtra("intent_type", "payment");
                launchIntent.putExtra("app_language_code", "en");
                launchIntent.putExtra("merchant_bank_id", "7b72c89e-e51f-4d26-97f2-d1b138c4b173");
                launchIntent.putExtra("solution_partner_id", "213b4089-d6da-11ef-959d-022df560c9d7")
                launchIntent.putExtra("device_setup_mode", 3);
                launchIntent.putExtra("trn_type", "100000001");
                launchIntent.putExtra("trn_currency", "EUR");
                launchIntent.putExtra("trn_amount", "19.99");
                launchIntent.putExtra("receipt_screen", true);

                activityLauncher.launch(launchIntent)
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
}