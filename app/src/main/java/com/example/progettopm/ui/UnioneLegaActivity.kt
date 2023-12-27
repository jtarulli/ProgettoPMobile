package com.example.progettopm.ui

import LegheAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Lega
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UnioneLegaActivity : AppCompatActivity() {

    private lateinit var legheAdapter: LegheAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unione_lega)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewLeghe)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inizializza l'adapter con un listener per il pulsante "Unisciti"
        legheAdapter = LegheAdapter { lega ->
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

    private fun uniscitiALega(lega: Lega) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Aggiungi l'ID della lega all'array 'leghe' dell'utente
            val utenteReference = FirebaseFirestore.getInstance().collection("utenti").document(userId)
            // Aggiungi l'ID della lega all'array 'leghe' dell'utente
            //utenteReference.update("leghe", FieldValue.arrayUnion(document.id))
              //  .addOnSuccessListener {
              //      Toast.makeText(this, "Ti sei unito a ${lega.nome}", Toast.LENGTH_SHORT).show()
             //   }
              //  .addOnFailureListener {
             //       Toast.makeText(this, "Errore durante l'aggiunta alla lega", Toast.LENGTH_SHORT).show()
              //  }
        }
    }


}
