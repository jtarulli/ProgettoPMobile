package com.example.progettopm.ui// com.example.progettopm.ui.CreazioneLegaActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R

class CreazioneLegaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_lega)

        val logoButton: Button = findViewById(R.id.logoButton)
        val nomeEditText: EditText = findViewById(R.id.nomeEditText)
        val budgetEditText: EditText = findViewById(R.id.budgetEditText)
        val parolaDOrdineEditText: EditText = findViewById(R.id.parolaDOrdineEditText)
        val confermaButton: Button = findViewById(R.id.confermaButton)

        logoButton.setOnClickListener {
            // Gestione del clic sul pulsante per selezionare il logo
            // ...
        }

        confermaButton.setOnClickListener {
            // Gestione del clic sul pulsante di conferma
            // Recupera i valori inseriti dall'utente
            val nome = nomeEditText.text.toString()
            val budget = budgetEditText.text.toString()
            val parolaDOrdine = parolaDOrdineEditText.text.toString()

            // Esegui il salvataggio dei dati nel database
            // ...

            // Poi puoi passare alla schermata successiva (com.example.progettopm.ui.CreazioneSquadraActivity)
            val intent = Intent(this, CreazioneSquadraActivity::class.java)
            intent.putExtra("nome", nome)
            intent.putExtra("budget", budget)
            intent.putExtra("parolaDOrdine", parolaDOrdine)
            startActivity(intent)
        }
    }
}
