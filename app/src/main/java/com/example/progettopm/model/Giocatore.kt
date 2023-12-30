package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Giocatore(
    val id: String = "", // Assicurati di inizializzare o fornire un valore predefinito
    val nome: String = "",
    val cognome: String = "",
    val lega: DocumentReference? = null,
    val valore: Int = 0,
    val foto: String = "",
    var punteggio: Int = 0,
    var bonusGiornataList: List<DocumentReference> = emptyList()
)
