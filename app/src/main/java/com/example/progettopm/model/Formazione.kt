package com.example.progettopm.model
import com.google.firebase.firestore.DocumentReference

data class Formazione(
    val id: String?,
    val giornata: DocumentReference,
    val user: DocumentReference,
    val punteggio: String,
    val nomeSquadra: String,
    val giocatoriSchierati: DocumentReference
)
