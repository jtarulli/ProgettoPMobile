package com.example.progettopm.model
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.google.firebase.firestore.DocumentReference

data class Formazione(
    val id: Int,
    val giornata: DocumentReference,
    val user: User,
    val punteggio: Int,
    val squadra: DocumentReference,
    val nomeSquadra: String,
    val giocatoriSchierati: DocumentReference
)
