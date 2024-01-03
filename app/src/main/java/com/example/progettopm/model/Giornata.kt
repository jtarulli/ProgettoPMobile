package com.example.progettopm.model

import java.time.Instant

data class Giornata(
    val id: Int,
    val inizio: Instant,
    val fine: Instant
)
