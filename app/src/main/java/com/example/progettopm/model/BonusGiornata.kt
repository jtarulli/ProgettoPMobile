package com.example.progettopm.model

import com.google.firebase.firestore.DocumentReference

data class BonusGiornata(
    val giocatore: String,
    val giornata: DocumentReference? = null,
    val quantita: Int = 0,
    val tipoBonus: DocumentReference? = null,
) {
    // Costruttore senza argomenti richiesto da Firestore
    constructor() : this("", null, 0, null)
}
