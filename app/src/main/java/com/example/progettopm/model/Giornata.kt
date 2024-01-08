package com.example.progettopm.model

import com.google.firebase.Timestamp

data class Giornata(
    var id: String = "",
    var numeroGiornata: Long = 0,
    val inizio: Timestamp = Timestamp.now(),
    val fine: Timestamp = Timestamp.now(),
    val lega: String = ""
)
