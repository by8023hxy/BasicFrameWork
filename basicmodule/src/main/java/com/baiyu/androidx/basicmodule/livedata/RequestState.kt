package com.baiyu.androidx.basicmodule.livedata

import com.baiyu.androidx.basicmodule.network.ApiException

sealed class RequestState<out T> {
    object Loading : RequestState<Nothing>()
    data class Success<out T>(val data: T?) : RequestState<T>()
    data class Error(val error: ApiException? = null) :
            RequestState<Nothing>()

}

