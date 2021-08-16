package com.nyan.domain.state

import com.nyan.domain.network.ErrorHandler

sealed class DataState<out R> {
    data class Success<out T>(val data: T): DataState<T>()
    data class Failed(val error: ErrorHandler): DataState<Nothing>()
    object Loading: DataState<Nothing>()
}