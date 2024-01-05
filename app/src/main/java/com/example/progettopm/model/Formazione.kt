package com.example.progettopm.model
import com.google.firebase.firestore.DocumentReference

data class Formazione(
    val legaId: String = "0",
    val giornata: Int = 0,
    val giocatori: List<DocumentReference>? = null, //Reference a giocatori
    val punteggio: Int = 0,
    val utente: String = "0",
    val squadra: DocumentReference?= null //Reference a squadra
)
