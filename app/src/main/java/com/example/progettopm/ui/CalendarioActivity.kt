package com.example.progettopm.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giornata
import com.example.progettopm.view.GiornataAdapter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.time.Instant

class CalendarioActivity : AppCompatActivity() {

    private lateinit var giornateAdapter: GiornataAdapter
    private lateinit var nuovoGiornoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_calendario)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCalendario)
        recyclerView.layoutManager = LinearLayoutManager(this)
        giornateAdapter = GiornataAdapter()
        recyclerView.adapter = giornateAdapter

        val giornate = FirebaseFirestore.getInstance().collection("giornate")
        giornate.addSnapshotListener{_, e ->
            if(e == null) {
                loadGiornate(giornate)
            }
        }
        loadGiornate(giornate)
    }

    // Funzione di esempio per ottenere una lista di giornate
    private fun loadGiornate(giornate : CollectionReference) {
        giornate.whereEqualTo("lega", SessionManager.legaCorrenteId)
                .get()
                .addOnCompleteListener { getGiornata(it.result)}
    }

    private fun getGiornata(documents : QuerySnapshot){
        val giornataList = mutableListOf<Giornata>()
        for (document in documents) {
            val giornata = Giornata(
                id = document["id"] as String,
                inizio = document.getTimestamp("inizio")!!,
                fine = document.getTimestamp("fine")!!,
                lega = document["lega"].toString()
            )
            giornataList.add(giornata)
        }
        giornataList.sortBy { giornata -> giornata.inizio }
        giornateAdapter.submitList(giornataList)
    }
}
