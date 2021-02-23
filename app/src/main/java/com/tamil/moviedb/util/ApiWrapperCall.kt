package com.tamil.moviedb.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * Wrapper class to handle Coroutine Exception
 */
suspend fun <T> apiWrapperCall(
    dispatcher: CoroutineDispatcher,
    api: suspend () -> T
): NetworkResource<T> {
    return withContext(dispatcher) {
        try {
            NetworkResource.Success(api.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> NetworkResource.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    NetworkResource.ResponseError(code, "Unable to process your request")
                }
                else -> {
                    NetworkResource.ResponseError(null, "Something went wrong")
                }
            }

        }
    }
}