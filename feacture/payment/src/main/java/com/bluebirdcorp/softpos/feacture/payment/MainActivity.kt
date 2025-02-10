package com.bluebirdcorp.softpos.feacture.payment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.domain.usecase.BarcodeHandleUsecase
import com.bluebirdcorp.softpos.feacture.payment.view_model.SharedPaymentViewModel

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = this::class.java.simpleName
    }



    @Inject
    lateinit var barcodeHandleUsecase: BarcodeHandleUsecase

    private lateinit var navController: NavController

    private val sharedPaymentViewModel: SharedPaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        debug("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // NavController 초기화
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        sharedPaymentViewModel.setActivityLauncher(mActivityLauncher)

        forceHideNavigationBar(this)
    }

    fun forceHideNavigationBar(activity: Activity) {
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE // 터치해도 네비게이션 바 다시 안 나타남
                )
    }


    override fun onResume() {
        super.onResume()
        barcodeHandleUsecase.openBarcode()
    }

    override fun onPause() {
        super.onPause()
        barcodeHandleUsecase.closeBarcode()
    }

    private val mActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "result.resultCode : ${result.resultCode}")
            navController.navigate(R.id.action_intro)
            when (result.resultCode) {
                RESULT_OK -> {
                    val data = result.data
                    val serverMessage =
                        data?.getStringExtra("server_message") ?: "No server message"
                    val intent_result = data?.getStringExtra("intent_result") ?: "No intent_result"
                    Log.d(TAG, "Transaction completed successfully. Server Message: $serverMessage")
                    Log.d(TAG, "Transaction completed successfully. intent_result: $intent_result")

                    Toast.makeText(
                        this,
                        "Server Message: $serverMessage, intent_result : $intent_result",
                        Toast.LENGTH_LONG
                    ).show()
                }
                RESULT_CANCELED -> {
                    val data = result.data
                    val serverMessage =
                        data?.getStringExtra("server_message") ?: "No server message"
                    Log.d(
                        TAG,
                        "Transaction was canceled by the user. Server Message: $serverMessage"
                    )
                }

                else -> {
                    val data = result.data
                    val serverMessage =
                        data?.getStringExtra("server_message") ?: "No server message"
                    Log.d(
                        TAG,
                        "Unexpected result code: ${result.resultCode}. Server Message: $serverMessage"
                    )
                }
            }
        }
}