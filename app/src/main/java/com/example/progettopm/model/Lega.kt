package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Lega(
    val nome: String,
    val budget: Int,
    val logo: String,
    val admin: String,  // Reference a utenti
    val numeroGiornate: Int,
    val giocatoriPerSquadra: Int
)