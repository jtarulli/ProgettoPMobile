package com.example.progettopm.model

data class Squadra(
    val id: String,
    val legaId: String,  // Utilizziamo String al posto di DocumentReference
    val allenatore: String,
    val nome: String,
    val logo: String,
    val punteggio: Int
)
