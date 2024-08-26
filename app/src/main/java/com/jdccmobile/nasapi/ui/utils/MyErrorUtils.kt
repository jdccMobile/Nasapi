package com.jdccmobile.nasapi.ui.utils

import com.jdccmobile.domain.model.MyError

fun MyError.toMessage(): String = when (this) {
    is MyError.Server -> "HTTP error $code"
    MyError.Connectivity -> "No internet connection"
    MyError.Generic -> "Unknown error.\nPlease try again later"
}
