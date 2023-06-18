package com.adelinarotaru.fooddelivery.utils

suspend fun <T> coRunCatching(codeToBeExecuted: suspend () -> T): Result<T> {
    return try {
        Result.success(codeToBeExecuted())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}