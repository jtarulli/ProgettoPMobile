package com.example.progettopm.ui// com.example.progettopm.ui.CreazioneSquadraActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R

class CreazioneSquadraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_squadra)

        val editTextNomeSquadra = findViewById<EditText>(R.id.editTextNomeSquadra)
        val buttonConfermaCreazione = findViewById<Button>(R.id.buttonConfermaCreazione)

        buttonConfermaCreazione.setOnClickListener {
            val nomeSquadra = editTextNomeSquadra.text.toString().trim()

            if (nomeSquadra.isNotEmpty()) {
                // Salva le informazioni della squadra nel database (implementa questa logica)
                // Assicurati di recuperare l'ID della squadra appena creata dal database
                val idSquadraAppenaCreata = saveSquadraInDatabase(nomeSquadra)

                // Reindirizza l'utente alla HomeActivity
                startHomeActivity(idSquadraAppenaCreata)
            } else {
                // Avvisa l'utente che deve inserire un nome squadra
                // Puoi mostrare un messaggio Toast o un altro tipo di avviso
            }
        }
    }

    private fun saveSquadraInDatabase(nomeSquadra: String): Int {
        // Implementa la logica per salvare la squadra nel tuo database
        // Qui dovresti inserire il nome della squadra nel database e ottenere l'ID assegnato
        // Restituisci l'ID della squadra appena creata
        return 123 // Sostituisci questo con l'ID reale ottenuto dal database
    }

    private fun startHomeActivity(idSquadra: Int) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("ID_SQUADRA", idSquadra)
        startActivity(intent)
        finish() // Opzionale: chiudi questa activity in modo che l'utente non possa tornare indietro
    }
}
