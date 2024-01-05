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
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Bonus
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField

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
        val bonus = intent.getStringExtra("bonus")

        // Log per debug
        Log.d("CreaBonusActivity", "Bonus object received: $bonus")

        // Imposta i valori nei campi di testo se l'oggetto Bonus esiste
        bonus?.let {
            FirebaseFirestore.getInstance()
                             .collection("bonus")
                             .document(bonus)
                             .get()
                             .addOnCompleteListener{ doc ->
                                 Log.d("Result", doc.result.toString())
                                 nomeEditText.setText(doc.result.get("nome").toString())
                                 valoreEditText.setText(doc.result.get("valore").toString())
                             }
        }

        // Aggiungi un listener al pulsante Conferma
        confermaButton.setOnClickListener {
            confermaModifica(bonus)
        }
    }

    private fun confermaModifica(bonusId : String? = "") {
        val nome = nomeEditText.text.toString().trim()
        val valore = valoreEditText.text.toString().toIntOrNull()

        if (nome.isEmpty() || valore == null) {
            // Gestisci il caso in cui i campi siano vuoti o il valore non sia un numero valido
            Log.d("CreaBonusActivity", "Invalid input detected")
            return
        }

        val lega = FirebaseFirestore.getInstance().collection("leghe")
                                                  .document(SessionManager.legaCorrenteId!!)

        // Crea un oggetto Bonus con i valori inseriti
        val nuovoBonus = Bonus(valore = valore, nome = nome, lega = lega)

        val bonusRef = FirebaseFirestore.getInstance().collection("bonus")
        if (bonusId != null) {
            nuovoBonus.id = bonusId
            bonusRef.document(bonusId)
                .set(nuovoBonus)
        } else {
            bonusRef.add(nuovoBonus)
                .addOnSuccessListener { documentReference ->
                    val nuovoId = documentReference.id
                    documentReference.update("id", nuovoId)
                }
        }

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
