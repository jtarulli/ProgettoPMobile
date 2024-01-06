package com.example.progettopm.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.example.progettopm.view.GiornataAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class CalendarioActivity : AppCompatActivity() {

    private lateinit var recyclerViewGiornate: RecyclerView
    private lateinit var giornateAdapter: GiornataAdapter
    private lateinit var nuovoGiornoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Activity_calendario", "Sono qui")
        setContentView(R.layout.fragment_calendario)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCalendario)
        recyclerView.layoutManager = LinearLayoutManager(this)
        giornateAdapter = GiornataAdapter()
        recyclerView.adapter = giornateAdapter
        loadGiornate()
    }

    // Funzione di esempio per ottenere una lista di giornate
    private fun loadGiornate() {
        Log.d("DATA_TEST", "" + SessionManager.legaCorrenteId)
        FirebaseFirestore.getInstance()
                         .collection("giornate")
                         .whereEqualTo("lega", SessionManager.legaCorrenteId)
                         .get()
                         .addOnCompleteListener { updateGiornata(it.result)}
    }

    private fun updateGiornata(documents : QuerySnapshot){
        val giornataList = mutableListOf<Giornata>()
        for (document in documents) {
            val giornata = Giornata(
                id = document["id"] as String,
                inizio = document.getTimestamp("inizio")!!.toDate().toInstant(),
                fine = document.getTimestamp("fine")!!.toDate().toInstant(),
                lega = document["lega"].toString()
            )
            giornataList.add(giornata)
        }
        Log.d("Debug_list", "" + giornataList)
        giornateAdapter.submitList(giornataList)
    }

    private fun getProssimoNumeroGiornata() {
        // Implementa la logica per ottenere il prossimo numero di giornata dalla tua sorgente dati
        // In questo esempio, restituiamo semplicemente il numero massimo + 1
        //return giornateAdapter.getGiornateList().maxByOrNull { it.id }?.id?.plus(1) ?: 1
    }
}
