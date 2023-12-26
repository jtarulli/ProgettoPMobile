package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Lega(
    val admin: DocumentReference,  // Cambia il tipo a DocumentReference
    val budget: Int,
    val logo: String,
    val nome: String,
    val numeroGiornate: Int,
    val giocatoriPerSquadra: Int
) {
    // Costruttore vuoto richiesto da Firestore
    constructor() : this(
        DocumentReference.Builder().build(),  // Inizializza con un'istanza vuota di DocumentReference
        0,
        "",
        "",
        0,
        0
    )
}
