package com.bluebirdcorp.softpos.feacture.payment.view_model

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.bluebirdcorp.softpos.barcode.const.TapxPhoneConstants
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.domain.usecase.BarcodeDatabaseUsecase
import com.bluebirdcorp.softpos.feacture.payment.fragment.PriceCheckerFragment
import org.json.JSONException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID
import javax.inject.Inject


class SharedPaymentViewModel : ViewModel() {

    private var activityLauncher: ActivityResultLauncher<Intent>? = null

    @Inject
    lateinit var barcodeDatabaseUsecase: BarcodeDatabaseUsecase

    fun setActivityLauncher(launcher: ActivityResultLauncher<Intent>) {
        this.activityLauncher = launcher
    }

    fun requestTapxPhonePayment(context: Context, currecyType: String, amount: Double) {
        debug("requestTapxPhonePayment! currecyType : $currecyType, amount : $amount")
        val launchIntent = context.packageManager.getLaunchIntentForPackage("by.iba.tapxphone")

        if (launchIntent != null) {
            launchIntent.flags = 0
            try {
                launchIntent.putExtra("version", "1.0.1")
                launchIntent.putExtra("intent_type", "payment")
                launchIntent.putExtra("app_language_code", "en")
//                launchIntent.putExtra("merchant_bank_id", "3afabe98-27e9-46cb-9bcc-2d2a0fcc4b9d")
                launchIntent.putExtra("merchant_bank_id", "3afabe98-27e9-46cb-9bcc-2d2a0fcc4b9d")
                launchIntent.putExtra("solution_partner_id", "213b4089-d6da-11ef-959d-022df560c9d7")
                launchIntent.putExtra("device_setup_mode", 3)
                launchIntent.putExtra("trn_type", "100000001")
                launchIntent.putExtra("trn_currency", PriceCheckerFragment.CURRENCY_EURO)
                launchIntent.putExtra("trn_amount", amount.toString())
                launchIntent.putExtra("receipt_screen", false)
                launchIntent.putExtra("cashier_name", "seongbin.choi@bluebirdcorp.com")

                debug("send intent : ${launchIntent.extras}")
                activityLauncher?.launch(launchIntent)
            } catch (e: JSONException) {
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

    fun requestWorldLinePayment(
        context: Context, currecyType: String, amount: Double
    ) {
        debug("requestWorldLinePayment! currecyType : $currecyType, amount : $amount")
        //10.22  -> 1022
        //13 -> 1300
        //14.99 -> 1499
        val intent = Intent("com.worldline.payment.action.PROCESS_TRANSACTION")
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.putExtra("WPI_SERVICE_TYPE", "WPI_SVC_PAYMENT")
        val transactionJson = """{
 "currency": "${PriceCheckerFragment.CURRENCY_EURO}",
 "requestedAmount": ${formatAndConvert(amount)},
 "reference": "nickseo0012",
 "receiptFormat": ["FORMATTED"],
 "tipAmount": 0
}"""

        intent.putExtra("WPI_REQUEST", transactionJson)
        intent.putExtra("WPI_VERSION", "2.1")
        //        intent.putExtra("WPI_SESSION_ID", System.currentTimeMillis());
        intent.putExtra("WPI_SESSION_ID", UUID.randomUUID().toString())

        debug("send intent : ${intent.extras}")


//        context.startActivity(intent)
        activityLauncher?.launch(intent)

//        startActivityResult.launch(intent)
//        launcher.launch(intent);
    }

    fun formatAndConvert(price: Double): Int {
        val formattedPrice = BigDecimal(price).setScale(2, RoundingMode.FLOOR) // 소수점 2자리에서 내림
        return (formattedPrice * BigDecimal(100)).toInt() // 정수 변환

    }
}