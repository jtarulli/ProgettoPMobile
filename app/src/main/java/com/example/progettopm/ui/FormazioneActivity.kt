package com.example.progettopm.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Formazione
import com.example.progettopm.model.Giocatore
import com.example.progettopm.view.SelezioneGiocatoriAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FormazioneActivity : AppCompatActivity() {

    private lateinit var confermaFormazioneButton: Button
    private lateinit var recyclerViewGiocatori: RecyclerView
    private lateinit var giocatoriAdapter: SelezioneGiocatoriAdapter
    private lateinit var giocatoriRimastiTextView: TextView
    private lateinit var budgetRimastoTextView: TextView
    private var budgetLega: Int = 0 // Aggiunto il campo budgetLega

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formazione)

        confermaFormazioneButton = findViewById(R.id.confermaFormazioneButton)
        recyclerViewGiocatori = findViewById(R.id.recyclerViewGiocatori)
        giocatoriRimastiTextView = findViewById(R.id.giocatoriRimastiTextView)
        budgetRimastoTextView = findViewById(R.id.budgetRimastoTextView)

        val legaCorrenteId = SessionManager.legaCorrenteId

        if (legaCorrenteId != null) {
            val legaRef = FirebaseFirestore.getInstance().document("leghe/$legaCorrenteId")

            legaRef.get().addOnSuccessListener { legaSnapshot ->
                val giocatoriPerSquadra = legaSnapshot.getLong("giocatoriPerSquadra") ?: 0
                val budgetLega = legaSnapshot.getLong("budget") ?: 0

                // Inizializza l'adapter con i valori ottenuti
                giocatoriAdapter = SelezioneGiocatoriAdapter(
                    giocatoriPerSquadra.toInt(),
                    giocatoriRimastiTextView,
                    budgetLega.toInt(),
                    budgetRimastoTextView
                )

                recyclerViewGiocatori.layoutManager = LinearLayoutManager(this)
                recyclerViewGiocatori.adapter = giocatoriAdapter

                // Aggiungi il listener per il click sul pulsante "Conferma Formazione"
                confermaFormazioneButton.setOnClickListener {
                    if (giocatoriAdapter.getGiocatoriSelezionati().size == giocatoriPerSquadra.toInt()) {
                        salvaFormazione(legaCorrenteId, giocatoriAdapter.getGiocatoriSelezionati())
                    } else {
                        Toast.makeText(
                            this,
                            "Finisci di selezionare i tuoi giocatori",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // Continua con il caricamento dei giocatori
                caricaGiocatori(legaCorrenteId)
            }
                .addOnFailureListener { exception ->
                    Log.e("FormazioneActivity", "Error loading lega", exception)
                }
        } else {
            Log.e("FormazioneActivity", "legaCorrenteId is null")
        }
    }


    private fun showMessaggioSuccesso() {
        // Implementa la visualizzazione di un messaggio di successo, ad esempio con un Toast
        Toast.makeText(this, "Formazione salvata con successo", Toast.LENGTH_SHORT).show()
    }

    private fun caricaGiocatori(legaCorrenteId: String?) {
        if (legaCorrenteId != null) {
            // Continua con il caricamento dei giocatori
            val giocatoriCollection = FirebaseFirestore.getInstance().collection("giocatori")

            giocatoriCollection
                .whereEqualTo(
                    "lega",
                    FirebaseFirestore.getInstance().document("leghe/$legaCorrenteId")
                )
                .get()
                .addOnSuccessListener { snapshot ->
                    val giocatoriList = mutableListOf<Giocatore>()

                    for (document in snapshot.documents) {
                        val giocatore = document.toObject(Giocatore::class.java)
                        if (giocatore != null) {
                            giocatoriList.add(giocatore)
                        }
                    }

                    giocatoriAdapter.submitList(giocatoriList)
                }
                .addOnFailureListener { exception ->
                    Log.e("FormazioneActivity", "Error loading giocatori", exception)
                }
        } else {
            Log.e("FormazioneActivity", "legaCorrenteId is null")
        }

    }
    private fun salvaFormazione(
        legaCorrenteId: String?,
        giocatoriSelezionati: List<DocumentReference>
    ) {
        if (legaCorrenteId != null) {
            val punteggioTotale = calcolaPunteggioTotale(giocatoriSelezionati)

            // Controlla se il punteggioTotale è maggiore del budgetLega
            if (punteggioTotale > budgetLega) {
                Toast.makeText(
                    this,
                    "Il punteggio totale supera il budget della lega",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            val formazione = Formazione(
                giornata = 1,
                giocatori = giocatoriSelezionati,
                punteggio = punteggioTotale,
                squadra = FirebaseFirestore.getInstance().document("squadre/${SessionManager.squadraCorrenteId}")
            )

            FirebaseFirestore.getInstance().collection("formazioni")
                .add(formazione)
                .addOnSuccessListener {
                    showMessaggioSuccesso()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("FormazioneActivity", "Error saving formazione", exception)
                }
        }
    }

    private fun calcolaPunteggioTotale(giocatori: List<DocumentReference>): Int {
        var punteggioTotale = 0
        var remainingCount = giocatori.size

        if (remainingCount == 0) {
            // Se non ci sono giocatori, restituisci 0
            return punteggioTotale
        }

        for (giocatoreRef in giocatori) {
            giocatoreRef.get().addOnSuccessListener { giocatoreDoc ->
                val punteggioGiocatore = giocatoreDoc.getLong("punteggio") ?: 0
                punteggioTotale += punteggioGiocatore.toInt()

                remainingCount--
                if (remainingCount == 0) {
                    // Callback per informare che il calcolo è completo
                    // Esempio: updateUI(punteggioTotale)
                }
            }
                .addOnFailureListener { exception ->
                    Log.e("FormazioneActivity", "Error getting punteggio", exception)
                }
        }

        return punteggioTotale
    }
}
