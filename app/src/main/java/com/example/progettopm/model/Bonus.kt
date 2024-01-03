package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Bonus(
    val documentId: String = "",
    val valore: Int = 0,
    val nome: String = "",
    val id: String = "",
    val lega: DocumentReference? = null,
)
