package com.example.progettopm.model
import com.google.firebase.firestore.DocumentReference

data class BonusGiornata(
    val id: String,
    val giocatore: DocumentReference,
    val giornata: DocumentReference,
    val quantita: Int,
    val tipoBonus: DocumentReference
)
