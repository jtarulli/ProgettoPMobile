package com.example.progettopm.model

import com.google.firebase.Timestamp
import java.time.Instant

data class Giornata(
    var id: String = "",
    val inizio: Timestamp = Timestamp.now(),
    val fine: Timestamp = Timestamp.now(),
    val lega: String = ""
)
