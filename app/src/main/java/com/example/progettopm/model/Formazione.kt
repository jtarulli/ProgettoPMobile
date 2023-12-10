import com.google.firebase.firestore.DocumentReference

data class Formazione(
    val id: Int,
    val giornata: DocumentReference,
    val portiere: DocumentReference,
    val difensore: DocumentReference,
    val laterale_dx: DocumentReference,
    val laterale_sx: DocumentReference,
    val attaccante: DocumentReference,
    val punteggio: Int,
    val squadra: DocumentReference
)
