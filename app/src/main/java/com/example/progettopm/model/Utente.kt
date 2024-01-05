package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Utente(
    val id: String = "",
    val nome: String = "",
    val cognome: String = "",
    val ruolo: String = "",
    val email: String = "",
    val password: String = "",
    val foto: String = "",
    val leghe: List<DocumentReference> ?= null,
    val squadre: List<DocumentReference> ?= null
)
