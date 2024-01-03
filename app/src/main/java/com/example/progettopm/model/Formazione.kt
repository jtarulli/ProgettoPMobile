package com.example.progettopm.model
import com.google.firebase.firestore.DocumentReference

data class Formazione(
    val giornata: Int,
    val giocatori: List<DocumentReference>, //Reference a giocatori
    val punteggio: Int,
    val squadra: DocumentReference //Reference a squadra
)
