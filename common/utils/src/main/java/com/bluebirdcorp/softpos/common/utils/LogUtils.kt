package com.bluebirdcorp.softpos.common.utils

import android.os.Build
import android.os.UserHandle
import android.util.Log

private const val TAG = "SoftposPaymentApp"

private fun getCallerInfo(cls: Class<*>? = null): String {
    val ste = Throwable().stackTrace
    val userType: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        if (UserHandle.getCallingUserId() == UserHandle.USER_SYSTEM) {
            "System user"
        } else {
            "Secondary user"
        }
    } else {
        null
    }
    return "[${cls?.simpleName ?: ste[3].fileName}]" +
            "[${ste[3].methodName}][${ste[3].lineNumber}][$userType]"
}

fun verbose(vararg any: Any) {
    StringBuilder().run {
        for (msg in any) {
            this.append(msg)
        }

        Log.v(TAG, "$this ${getCallerInfo()}")
    }
}

fun debug(msg: String) {
    Log.d(TAG, "$msg ${getCallerInfo()}")
}

fun debug(cls: Class<*>, msg: String) {
    Log.d(TAG, "$msg ${getCallerInfo(cls)}")
}

fun debug(vararg any: Any) {
    StringBuilder().run {
        for (msg in any) {
            this.append(msg)
        }

        Log.d(TAG, "$this ${getCallerInfo()}")
    }
}

fun info(msg: String) {
    Log.i(TAG, "$msg ${getCallerInfo()}")
}

fun info(cls: Class<*>, msg: String) {
    Log.i(TAG, "$msg ${getCallerInfo(cls)}")
}

fun info(vararg any: Any) {
    StringBuilder().run {
        for (msg in any) {
            this.append(msg)
        }

        Log.i(TAG, "$this ${getCallerInfo()}")
    }
}

fun warn(msg: String) {
    Log.w(TAG, "$msg ${getCallerInfo()}")
}

fun warn(vararg any: Any) {
    StringBuilder().run {
        for (msg in any) {
            this.append(msg)
        }

        Log.w(TAG, "$this ${getCallerInfo()}")
    }
}

fun errorLog(msg: String) {
    Log.e(TAG, "$msg ${getCallerInfo()}")
}

fun errorLog(vararg any: Any) {
    StringBuilder().run {
        for (msg in any) {
            this.append(msg)
        }

        Log.e(TAG, "$this ${getCallerInfo()}")
    }
}
