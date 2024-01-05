package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Formazione(
    var id: String = "",  // o un valore di default appropriato
    val giornata: Int = 0,
    val giocatori: List<DocumentReference> = emptyList(),
    val punteggio: Int = 0,
    val squadra: DocumentReference? = null
)
