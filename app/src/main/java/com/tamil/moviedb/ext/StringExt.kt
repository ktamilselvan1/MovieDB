package com.tamil.moviedb.ext

import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.tamil.moviedb.data.remote.responses.GenresItem
import com.tamil.moviedb.data.remote.responses.ProductionCompaniesItem


fun List<ProductionCompaniesItem>.getCommaSeparatedCompanies(): String {
    return this.map {
        it.name
    }.joinToString(", ")
}

fun List<GenresItem>.getCommaSeparatedGenres(): String {
    return this.map {
        it.name
    }.joinToString(", ")
}

fun String.toBold(): SpannableString {
    val index = this.indexOf(":")
    val spannableString = SpannableString(this)
    spannableString.setSpan(
        StyleSpan(BOLD),
        0,
        this.indexOf(":"),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}