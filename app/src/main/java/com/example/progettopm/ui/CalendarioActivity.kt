package com.example.progettopm.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Giornata
import com.example.progettopm.view.GiornateAdapter
import java.text.SimpleDateFormat
import java.util.*

class CalendarioActivity : AppCompatActivity() {

    private lateinit var recyclerViewGiornate: RecyclerView
    private lateinit var giornateAdapter: GiornateAdapter
    private lateinit var nuovoGiornoButton: Button

    class CalendarioActivity : AppCompatActivity() {

        private lateinit var recyclerViewGiornate: RecyclerView
        private lateinit var giornateAdapter: GiornateAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_calendario)

            recyclerViewGiornate = findViewById(R.id.recyclerViewGiornate)

            // Assume che tu abbia una lista di giornate
            val giornateList = getGiornateList()

            giornateAdapter = GiornateAdapter(giornateList)
            recyclerViewGiornate.layoutManager = LinearLayoutManager(this)
            recyclerViewGiornate.adapter = giornateAdapter

            // Aggiungi un listener al pulsante per creare nuove giornate
            val nuovoGiornoButton = findViewById<Button>(R.id.nuovoGiornoButton)
            nuovoGiornoButton.setOnClickListener {
                // Implementa la logica per creare una nuova giornata e aggiungerla alla lista
                // Assicurati di aggiornare la tua RecyclerView con la nuova lista di giornate
                // Ad esempio, potresti lanciare una nuova activity per la creazione di giornate
                // e ottenere il risultato con onActivityResult
                // onActivityResult sarebbe chiamato quando l'utente completa la creazione della giornata
            }
        }

        // Funzione di esempio per ottenere una lista di giornate
        private fun getGiornateList(): List<Giornata> {
            // Implementa la logica per ottenere la lista di giornate dalla tua sorgente dati
            // In questo esempio, creiamo alcune giornate di esempio
            return listOf(
                // Aggiungi altre giornate secondo le tue esigenze
            )
        }
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
