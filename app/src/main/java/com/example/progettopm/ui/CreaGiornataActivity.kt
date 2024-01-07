// CreaGiornataActivity.kt
package com.example.progettopm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giornata
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant

class CreaGiornataActivity : AppCompatActivity() {

    private lateinit var inizioEditText: EditText
    private lateinit var fineEditText: EditText
    private lateinit var confermaButton: Button

    private var giornata: Giornata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crea_giornata)

        inizioEditText = findViewById(R.id.editTextInizio)
        fineEditText = findViewById(R.id.editTextFine)
        confermaButton = findViewById(R.id.buttonConfermaGiornata)

        // giornata = intent.getParcelableExtra("GIORNATA")
        Log.d("CreaGiornataActivity", "Giornata object received: $giornata")

        giornata?.let {
            inizioEditText.setText(it.inizio.toString())
            fineEditText.setText(it.fine.toString())
        }

        confermaButton.setOnClickListener {
            confermaModifica()
        }
    }

    private fun confermaModifica() {
        val inizio = Instant.parse(inizioEditText.text.toString().trim())
        val fine = Instant.parse(fineEditText.text.toString().trim())

        val legaId = FirebaseFirestore.getInstance().collection("leghe")
            .document(SessionManager.legaCorrenteId!!)

        //val nuovaGiornata = Giornata(inizio = inizio, fine = fine, lega = legaId.id)

        //FirebaseFirestore.getInstance().collection("giornate").add(nuovaGiornata)
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CreaGiornataActivity::class.java)
        }

        fun editIntent(context: Context, giornata: Giornata): Intent {
            val intent = Intent(context, CreaGiornataActivity::class.java)
            // intent.putExtra("GIORNATA", giornata)
            return intent
        }
    }
}
