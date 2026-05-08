package com.shekhargh.reminderApp.data.usecase

sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    data object Empty : Result<Nothing>
}

suspend fun <T> asResult(block: suspend () -> T?): Result<T> {
    return try {
        block()?.let { Result.Success(it) } ?: Result.Empty
    } catch (e: Exception) {
        Result.Error(e)
    }
}
