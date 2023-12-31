package com.example.progettopm.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.fragments.mainFragments.HomeFragment
import com.example.progettopm.model.Lega
import com.example.progettopm.view.MasterActivity
import com.example.progettopm.view.UnioneLegaAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UnioneLegaActivity : AppCompatActivity() {

    private lateinit var legheAdapter: UnioneLegaAdapter
    val legheList = mutableListOf<Lega>()

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

        //search view
        val searchView = findViewById<SearchView>(R.id.unioneLega_searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filteredLegaList = legheList.filter { lega ->
                    lega.nome.contains(newText, ignoreCase = true)
                }
                legheAdapter.submitList(filteredLegaList)
                return true
            }
        })


    }

    private fun caricaLeghe() {
        val legheCollection = FirebaseFirestore.getInstance().collection("leghe")

        legheCollection.get()
            .addOnSuccessListener { result ->
               // val legheList = mutableListOf<Lega>()

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
            var unisciti_btn: Button = findViewById(R.id.uniscitiButton)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val legheUtente = document.get("leghe") as List<String>?
                        unisciti_btn.isEnabled = !(legheUtente != null && legheUtente.contains(legaScelta.id))

                        // Cambia il colore del pulsante in base allo stato abilitato/disabilitato
                        if (!unisciti_btn.isEnabled) {
                            unisciti_btn.setBackgroundColor(resources.getColor(R.color.bottoneDisabilitatoSfondo))
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Errore nel recupero dei dati dell'utente: ", exception)
                }
        }
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




