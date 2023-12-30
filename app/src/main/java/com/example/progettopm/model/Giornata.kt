package com.example.progettopm.model
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.google.firebase.firestore.DocumentReference
data class Giornata(
    val id: Int,
    val inizio: Long, //in kotlin si usa long per i timeStamp
    val fine: Long
    )
