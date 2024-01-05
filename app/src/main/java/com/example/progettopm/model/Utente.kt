package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Utente(
    val id: String,  // Cambiato da Int a String
    val nome: String,
    val cognome: String,
    val ruolo: String,
    val email: String,
    val password: String,
    val foto: String,
    val leghe: List<DocumentReference>,  // Cambiato da Array a Lis
    val squadre: List<DocumentReference>
)
