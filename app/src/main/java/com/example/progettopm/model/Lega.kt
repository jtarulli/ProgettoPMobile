package com.example.progettopm.model
import com.google.firebase.firestore.DocumentReference
data class Lega(
    val id: Int,
    val admin: DocumentReference,  // Reference a utenti
    val budget: Int,
    val logo: String,
    val nome: String,
    val password: String,
    val tipo: Boolean
)