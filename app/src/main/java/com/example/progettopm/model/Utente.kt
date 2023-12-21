package com.example.progettopm.model
import com.google.firebase.firestore.DocumentReference
data class Utente(
    val id: Int,
    val nome: String,
    val cognome: String,
    val ruolo: String,
    val username: String,
    val email: String,
    val password: String,
    val foto: String
)