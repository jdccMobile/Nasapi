package com.jdccmobile.data.utils

import com.jdccmobile.domain.model.MyError
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toMyError(): MyError = when (this) {
        is HttpException -> MyError.Server(code())
        is IOException -> MyError.Connectivity
        else -> MyError.Generic
    }
