package com.example.progettopm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giornata
import com.example.progettopm.view.GiornateAdapter
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant

class CalendarioActivity : AppCompatActivity(), GiornateAdapter.GiornataListener {

    private lateinit var recyclerViewGiornate: RecyclerView
    private lateinit var giornateAdapter: GiornateAdapter
    private lateinit var nuovoGiornoButton: Button
    private lateinit var legaId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        recyclerViewGiornate = findViewById(R.id.recyclerViewGiornate)
        nuovoGiornoButton = findViewById(R.id.nuovoGiornoButton)

        legaId = SessionManager.legaCorrenteId ?: ""

        val giornateList = getGiornateList(legaId)

        giornateAdapter = GiornateAdapter(giornateList, this)
        recyclerViewGiornate.layoutManager = LinearLayoutManager(this)
        recyclerViewGiornate.adapter = giornateAdapter

        nuovoGiornoButton.setOnClickListener {
            // Avvia l'activity per la creazione di una nuova giornata
            startActivity(Intent(this, CreaGiornataActivity::class.java))
        }
    }

    private fun getGiornateList(legaId: String): List<Giornata> {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("giornate")
            .whereEqualTo("lega", legaId)
            .get()
            .addOnSuccessListener { documents ->
                val giornateList = mutableListOf<Giornata>()
                for (document in documents) {
                    val id = document.getLong("id")?.toInt() ?: 0
                    val inizio = document.getDate("inizio")?.toInstant() ?: Instant.now()
                    val fine = document.getDate("fine")?.toInstant() ?: Instant.now()
                    val lega = document.getString("lega") ?: ""

                    val giornata = Giornata(id, inizio, fine, lega)
                    giornateList.add(giornata)
                }

                giornateAdapter.updateList(giornateList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Errore nel recupero delle giornate: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        return emptyList()
    }

    // Implementazione delle azioni Modifica ed Elimina
    override fun onModificaClick(position: Int) {
        // Implementa l'azione di modifica qui
        Toast.makeText(this, "Modifica Giornata alla posizione $position", Toast.LENGTH_SHORT).show()
    }

    override fun onEliminaClick(position: Int) {
        val giornataDaEliminare = giornateAdapter.getGiornateList()[position]

        // Aggiungi il codice per eliminare la giornata da Firebase Firestore
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("giornate")
            .document(giornataDaEliminare.id.toString()) // Supponendo che l'ID del documento sia la rappresentazione stringa dell'ID della giornata
            .delete()
            .addOnSuccessListener {
                // Elimina la giornata dalla lista dell'adapter
                giornateAdapter.rimuoviGiornata(giornataDaEliminare)
                Toast.makeText(this, "Giornata eliminata con successo", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Errore durante l'eliminazione della giornata: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
