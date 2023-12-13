package com.example.progettopm.model
import com.google.firebase.firestore.DocumentReference

data class Squadra(
    val id: String,
    val lega: DocumentReference,  // Reference a leghe
    val allenatore: DocumentReference,  // Reference a utenti
    val nome: String,
    val logo: String,
    val punteggio: Int
)