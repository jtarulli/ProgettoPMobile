package com.example.progettopm.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.MyDatePicker
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giornata
import com.example.progettopm.view.GiornataAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.time.Instant
import java.time.ZoneOffset

class CalendarioActivity : AppCompatActivity() {

    private lateinit var giornateAdapter: GiornataAdapter
    private lateinit var nuovoGiornoButton: Button
    private lateinit var inizioDate: Timestamp
    private lateinit var fineDate: Timestamp
    private var lastInsertData: Giornata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_calendario)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCalendario)
        recyclerView.layoutManager = LinearLayoutManager(this)
        giornateAdapter = GiornataAdapter()
        recyclerView.adapter = giornateAdapter

        val giornate = FirebaseFirestore.getInstance().collection("giornate")
        giornate.addSnapshotListener { _, e ->
            if (e == null) {
                loadGiornate(giornate)
            }
        }
        loadGiornate(giornate)

        nuovoGiornoButton = findViewById(R.id.nuovoGiornoButton)
        nuovoGiornoButton.setOnClickListener {
            val inizioPicker = MyDatePicker("Inizio",this)
            inizioPicker.setOnDateChangedListener {
                inizioDate = Timestamp(inizioPicker.getTimeLocal().toEpochSecond(ZoneOffset.UTC), 0)
            }
            val finePicker = MyDatePicker("Fine",this)
            finePicker.setOnDateChangedListener {
                fineDate = Timestamp(finePicker.getTimeLocal().toEpochSecond(ZoneOffset.UTC), 0)
                pushMod(inizioDate, fineDate)
            }
            finePicker.mostraSceltaDate()
            inizioPicker.mostraSceltaDate()
        }
    }

    // Funzione di esempio per ottenere una lista di giornate
    private fun loadGiornate(giornate: CollectionReference) {
        giornate.whereEqualTo("lega", SessionManager.legaCorrenteId)
            .get()
            .addOnCompleteListener { getGiornata(it.result) }
    }

    private fun getGiornata(documents: QuerySnapshot) {
        val giornataList = mutableListOf<Giornata>()
        for (document in documents) {
            val giornata = Giornata(
                id = document["id"] as String,
                inizio = document.getTimestamp("inizio")!!,
                fine = document.getTimestamp("fine")!!,
                numeroGiornata = document["numeroGiornata"] as Long,
                lega = document["lega"].toString()
            )
            giornataList.add(giornata)
        }
        giornataList.sortBy { giornata -> giornata.inizio }
        lastInsertData = giornataList.lastOrNull()
        giornateAdapter.submitList(giornataList)
    }

    private fun pushMod(inizio: Timestamp, fine: Timestamp) {
        if (inizio < fine &&
            (lastInsertData?.fine ?: Timestamp(Instant.EPOCH.epochSecond, 0)) < inizio) {
            val updateGiornata = Giornata(
                inizio = inizio,
                fine = fine,
                lega = SessionManager.legaCorrenteId!!
            )
            FirebaseFirestore.getInstance().collection("giornate")
                .add(updateGiornata)
                .addOnCompleteListener {
                    it.result.update("numeroGiornata", lastInsertData!!.numeroGiornata+1)
                    it.result.update("id", it.result.id)
                }
        } else {
            Toast.makeText(this, "La data non è accettabile", Toast.LENGTH_SHORT).show()
        }
    }
}
