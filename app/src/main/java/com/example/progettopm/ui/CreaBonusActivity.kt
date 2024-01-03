package com.example.progettopm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.example.progettopm.model.Bonus

class CreaBonusActivity : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var valoreEditText: EditText
    private lateinit var confermaButton: Button

    private var bonus: Bonus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crea_bonus)

        // Inizializza i componenti UI
        nomeEditText = findViewById(R.id.editTextNome)
        valoreEditText = findViewById(R.id.editTextValore)
        confermaButton = findViewById(R.id.buttonConferma)

        // Recupera l'oggetto Bonus passato dall'intent, se presente
        // bonus = intent.getParcelableExtra("BONUS")

        // Log per debug
        Log.d("CreaBonusActivity", "Bonus object received: $bonus")

        // Imposta i valori nei campi di testo se l'oggetto Bonus esiste
        bonus?.let {
            nomeEditText.setText(it.nome)
            valoreEditText.setText(it.valore.toString())
        }

        // Aggiungi un listener al pulsante Conferma
        confermaButton.setOnClickListener {
            confermaModifica()
        }
    }

    private fun confermaModifica() {
        val nome = nomeEditText.text.toString().trim()
        val valore = valoreEditText.text.toString().toIntOrNull()

        if (nome.isEmpty() || valore == null) {
            // Gestisci il caso in cui i campi siano vuoti o il valore non sia un numero valido
            Log.d("CreaBonusActivity", "Invalid input detected")
            return
        }

        // Crea un oggetto Bonus con i valori inseriti
        val nuovoBonus = Bonus(valore = valore, nome = nome)

        // Log per debug
        Log.d("CreaBonusActivity", "New Bonus object created: $nuovoBonus")

        // Passa il bonus modificato/creato all'activity chiamante utilizzando un Bundle
        val resultIntent = Intent()
        resultIntent.putExtra("NUOVO_BONUS", nuovoBonus as Parcelable)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CreaBonusActivity::class.java)
        }

        fun editIntent(context: Context, bonus: Bonus): Intent {
            val intent = Intent(context, CreaBonusActivity::class.java)
            // intent.putExtra("BONUS", bonus)
            return intent
        }
    }
}
