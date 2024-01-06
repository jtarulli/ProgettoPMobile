package com.example.progettopm.model

import java.time.Instant

data class Giornata(
    var id: String = "",
    val inizio: Instant = Instant.EPOCH,
    val fine: Instant = Instant.EPOCH,
    val lega: String = ""
)
