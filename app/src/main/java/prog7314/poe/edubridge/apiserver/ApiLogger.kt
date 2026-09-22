package prog7314.poe.edubridge.apiserver

import android.util.Log

object ApiLogger {

    fun d(message: String) = Log.d(ApiConfig.TAG, message)

    fun i(message: String) = Log.i(ApiConfig.TAG, message)

    fun w(message: String) = Log.w(ApiConfig.TAG, message)

    fun e(message: String, throwable: Throwable? = null) = Log.e(ApiConfig.TAG, message, throwable)
}