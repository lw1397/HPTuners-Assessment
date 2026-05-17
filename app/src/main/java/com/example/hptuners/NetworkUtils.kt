package com.example.hptuners

enum class Status {
    SUCCESS, FAILURE, LOADING,
}

class UiState<T>(
    val status: Status = Status.LOADING,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T): UiState<T> =
            UiState(status = Status.SUCCESS, data = data)

        fun <T> failure(): UiState<T> =
            UiState(status = Status.FAILURE, data = null)

        fun <T> loading(): UiState<T> =
            UiState(status = Status.LOADING, data = null)
    }
}