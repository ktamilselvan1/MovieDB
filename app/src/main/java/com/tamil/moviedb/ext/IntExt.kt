package com.tamil.moviedb.ext

fun Int.getDuration(): String {
    return "${String.format("%02d", this / 60)} : ${String.format("%02d", this % 60)}"
}