package com.example.progettopm.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giornata
import com.example.progettopm.view.BonusAdapter
import com.example.progettopm.view.GiornateAdapter
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class CalendarioActivity : AppCompatActivity() {

    private lateinit var recyclerViewGiornate: RecyclerView
    private lateinit var giornateAdapter: GiornateAdapter
    private lateinit var nuovoGiornoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Activity_calendario", "Sono qui")
        setContentView(R.layout.fragment_calendario)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCalendario)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Assume che tu abbia una lista di giornate
        val giornate = getGiornateList()
        //giornateAdapter = GiornateAdapter(giornate)
        //recyclerView.adapter = giornateAdapter
    }

    // Funzione di esempio per ottenere una lista di giornate
    private fun getGiornateList() : List<Giornata> {
        val giornate : MutableList<Giornata> = mutableListOf()
        FirebaseFirestore.getInstance()
                         .collection("giornate")
                         .whereEqualTo("lega", SessionManager.legaCorrenteId)
                         .get().addOnCompleteListener { q ->
                giornate.addAll(q.result.toObjects(Giornata::class.java))
        }
        return giornate
    }

    private fun mostraSceltaDate() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                mostraSceltaOraInizio(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun mostraSceltaOraInizio(calendar: Calendar) {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val inizio = calendar.timeInMillis
                mostraSceltaOraFine(inizio)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun mostraSceltaOraFine(inizio: Long) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val fine = calendar.timeInMillis

                if (fine > inizio) {
                    // Creare una nuova giornata solo se la fine è successiva all'inizio
                    //val nuovaGiornata = Giornata(getProssimoNumeroGiornata(), inizio, fine)
                    //giornateAdapter.aggiungiGiornata(nuovaGiornata)
                } else {
                    // Visualizza un messaggio di errore
                    // Implementa il feedback utente a tuo piacimento
                    // Ad esempio, mostra un Toast
                    Toast.makeText(this, "La data di fine deve essere successiva a quella di inizio", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun getProssimoNumeroGiornata() {
        // Implementa la logica per ottenere il prossimo numero di giornata dalla tua sorgente dati
        // In questo esempio, restituiamo semplicemente il numero massimo + 1
        //return giornateAdapter.getGiornateList().maxByOrNull { it.id }?.id?.plus(1) ?: 1
    }
}
