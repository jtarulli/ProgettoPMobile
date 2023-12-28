package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

// Giocatore.kt
data class Giocatore(
    val nome: String,
    val cognome: String,
    val lega: DocumentReference,
    val valore: Int,
    val foto: String,
    val punteggio: Int
)
