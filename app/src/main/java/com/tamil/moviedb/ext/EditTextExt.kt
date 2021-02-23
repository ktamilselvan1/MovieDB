package com.tamil.moviedb.ext

import android.util.Log
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

private const val DEBOUNCE_PERIOD: Long = 1500

fun SearchView.queryChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            return true
        }
    })
    return query
}

fun SearchView.performDelayedSearch(delay: Long = DEBOUNCE_PERIOD): Flow<String> {
    return queryChangeStateFlow()
        .debounce(delay)
        .filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .flowOn(Dispatchers.Default)
}