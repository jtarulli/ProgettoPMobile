package com.example.progettopm.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.fragments.HomeFragment
import com.example.progettopm.model.Lega
import com.example.progettopm.view.MasterActivity
import com.example.progettopm.view.UnioneLegaAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UnioneLegaActivity : AppCompatActivity() {

    private lateinit var legheAdapter: UnioneLegaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unione_lega)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewLeghe)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inizializza l'adapter con un listener per il pulsante "Unisciti"
        legheAdapter = UnioneLegaAdapter { lega ->
            uniscitiALega(lega)
        }

        recyclerView.adapter = legheAdapter

        // Carica le leghe dalla Firestore e aggiorna l'adapter
        caricaLeghe()
    }

    private fun caricaLeghe() {
        val legheCollection = FirebaseFirestore.getInstance().collection("leghe")

        legheCollection.get()
            .addOnSuccessListener { result ->
                val legheList = mutableListOf<Lega>()

                for (document in result) {
                    // Converti ogni documento in un oggetto Lega
                    val lega = document.toObject(Lega::class.java)
                    legheList.add(lega)
                }

                // Aggiorna l'adapter con la nuova lista di leghe
                legheAdapter.submitList(legheList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Errore nel recupero delle leghe: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uniscitiALega(legaScelta: Lega) {
        // Ottieni il riferimento al documento della lega scelta
        val legaRef = FirebaseFirestore.getInstance().collection("leghe").document(legaScelta.id)

        // Aggiorna il campo "leghe" dell'utente corrente con il riferimento al documento della lega scelta
        val user = FirebaseAuth.getInstance().currentUser
        val userDocRef = user?.let { FirebaseFirestore.getInstance().collection("utenti").document(it.uid) }

        if (userDocRef != null) {
            userDocRef.update("leghe", FieldValue.arrayUnion(legaRef))
                .addOnSuccessListener {
                    // Aggiornamento riuscito
                    Toast.makeText(this, "Ti sei unito alla lega ${legaScelta.nome}", Toast.LENGTH_SHORT).show()

                    // Se l'aggiornamento ha successo, esegui altre azioni qui
                }
                .addOnFailureListener { e ->
                    // Gestione dell'errore durante l'aggiornamento
                    Log.w(TAG, "Errore durante l'aggiornamento del campo leghe", e)
                    Toast.makeText(this, "Si è verificato un errore, riprova più tardi", Toast.LENGTH_SHORT).show()
                }
        }
    }

}



