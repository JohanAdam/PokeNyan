package com.nyan.domain.network

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

/**
 * Handles error during api calls.
 */
class ErrorHandler(e: Exception) : Exception(e) {

    companion object {
        const val DEFAULT_ERROR_MESSAGE: String = "An Error has occurred. Please try again later."
        const val NO_NETWORK_ERROR_MESSAGE: String = "No internet connection detected. Please try again later."
        const val BAD_REQUEST_ERROR_MESSAGE: String = "Oh no. Bad Request."

        const val RESPONSE_CODE_NO_INTERNET = 1
        const val RESPONSE_CODE_FAILED = 2
        const val RESPONSE_CODE_BAD_REQUEST = 400
    }

    private val error: Throwable?

    init {
        e.printStackTrace()
        error = e
    }

    val responseCode: Int
        get() {
            return if (error is HttpException) {
                error.code()
            } else if (error is IOException || error is UnknownHostException) {
                RESPONSE_CODE_NO_INTERNET
            } else {
                RESPONSE_CODE_FAILED
            }
        }

    val errorMsg: String get() {
        if (responseCode.equals(RESPONSE_CODE_BAD_REQUEST)) {
            return BAD_REQUEST_ERROR_MESSAGE
        } else if (responseCode.equals(RESPONSE_CODE_FAILED)) {
            return DEFAULT_ERROR_MESSAGE
        } else if (responseCode.equals(RESPONSE_CODE_NO_INTERNET)) {
            return NO_NETWORK_ERROR_MESSAGE
        } else {
            return error!!.message!!
        }
    }
}