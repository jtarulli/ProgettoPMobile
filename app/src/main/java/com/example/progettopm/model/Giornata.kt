package com.example.progettopm.model

import java.time.Instant

data class Giornata(
    val id: Int,
    val inizio: Instant,
    val fine: Instant,
    val legaId: String // Aggiunto campo legaId per identificare a quale lega appartiene la giornata
)
