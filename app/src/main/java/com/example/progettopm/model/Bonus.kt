package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class Bonus(
    val valore: Int = 0,

    val nome: String = "",
    var id: String = "",
    val lega: DocumentReference? = null,
)
