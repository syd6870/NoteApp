package com.example.noteapp.util

import androidx.appcompat.widget.SearchView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

inline fun SearchView.onQueryTextChanged(crossinline listener:(String)->Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }

    })
}

fun Long.toDate():String{
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("EN", "IN"))
    return simpleDateFormat.format(this)
}

fun Long.toTime():String{
    val simpleTimeFormat = SimpleDateFormat("hh:mm a", Locale("EN", "IN"))
    return simpleTimeFormat.format(this)
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun Float.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}