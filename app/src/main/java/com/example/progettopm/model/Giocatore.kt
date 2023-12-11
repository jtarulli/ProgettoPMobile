package com.example.progettopm.model

// Giocatore.kt
data class Giocatore(
    val id: Int,
    val nome: String,
    val cognome: String,
    val squadra: String,
    val ruolo: String,
    val valore: Int,
    val foto: String,
    val punteggio: Int
)
