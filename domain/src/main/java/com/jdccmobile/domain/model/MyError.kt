package com.jdccmobile.domain.model

sealed interface MyError {
    data class Server(val code: Int) : MyError

    data object Connectivity : MyError

    data object Generic : MyError
}
