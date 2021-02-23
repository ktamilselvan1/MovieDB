package com.tamil.moviedb.util

/**
 * Sealed class to differentiate [Success] [ResponseError] and [NetworkError]
 */
sealed class NetworkResource<out T> {
    data class Success<out T>(val value: T) : NetworkResource<T>()
    data class ResponseError(val code: Int?, val message: String?) : NetworkResource<Nothing>()
    object NetworkError : NetworkResource<Nothing>()
}
