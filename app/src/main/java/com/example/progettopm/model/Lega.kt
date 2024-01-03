package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Lega(
    val id: String = "",
    val nome: String = "",
    val budget: Int = 0,
    val logo: String = "",
    val admin: String = "",
    val numeroGiornate: Int = 0,
    val giocatoriPerSquadra: Int = 0
)