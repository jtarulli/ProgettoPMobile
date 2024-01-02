package com.example.progettopm.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ModificaLegaActivity : AppCompatActivity() {

    private var uriLogo: Uri? = null
    private lateinit var nomeEditText: EditText
    private lateinit var budgetEditText: EditText
    private lateinit var giocatoriPerSquadraEditText: EditText
    private lateinit var numeroGiornateEditText: EditText
    private lateinit var logoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifica_lega)

        // Recupera l'ID della lega da modificare dall'intent
        val legaId = SessionManager.legaCorrenteId

        // Inizializza i componenti UI
        nomeEditText = findViewById(R.id.nomeEditText)
        budgetEditText = findViewById(R.id.budgetEditText)
        giocatoriPerSquadraEditText = findViewById(R.id.giocatoriPerSquadraEditText)
        numeroGiornateEditText = findViewById(R.id.numeroGiornateEditText)
        logoImageView = findViewById(R.id.logoLegaImageView)
        val logoButton: Button = findViewById(R.id.logoButton)
        val confermaButton: Button = findViewById(R.id.confermaButton)

        Log.d("ModificaLegaActivity", "sei dentro")

        // Recupera i dettagli della lega dal Firestore
        retrieveLegaDetails(legaId)

        // Registro il result launcher per ottenere l'URI dell'immagine dalla galleria
        val galleryImage =
            registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
                logoImageView.setImageURI(it)
                if (it != null) {
                    uriLogo = it
                }
            })

        logoButton.setOnClickListener {
            // Avvia la galleria per la selezione dell'immagine
            galleryImage.launch("image/*")
        }

        confermaButton.setOnClickListener {
            // Recupera i valori inseriti dall'utente
            val nome = nomeEditText.text.toString().trim()
            val budget = budgetEditText.text.toString().trim().toIntOrNull()
            val giocatoriPerSquadra =
                giocatoriPerSquadraEditText.text.toString().trim().toIntOrNull()
            val numeroGiornate = numeroGiornateEditText.text.toString().trim().toIntOrNull()

            // Verifica che tutti i campi siano stati compilati
            if (nome.isEmpty() || budget == null || giocatoriPerSquadra == null || numeroGiornate == null) {
                Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (legaId != null) {
                modificaLega(legaId, nome, budget, giocatoriPerSquadra, numeroGiornate)
            } else {
                // Gestire il caso in cui legaId sia null, ad esempio mostrare un messaggio di errore
                Toast.makeText(
                    this,
                    "Errore nel recupero dell'ID della lega",
                    Toast.LENGTH_SHORT
                ).show()

            }
            finish()
        }
    }

    private fun retrieveLegaDetails(legaId: String?) {
        Log.d("ModificaLegaActivity", "sei in retrieveLegaDetails")
        if (legaId != null) {
            Log.d("ModificaLegaActivity", "sei in retrieveLegaDetails dentro al if (legaId != null)")
            val legaRef = FirebaseFirestore.getInstance().collection("leghe").document(legaId)

            legaRef.get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot? ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Log.d("ModificaLegaActivity", "sei in retrieveLegaDetails dentro al if (documentSnapshot != null && documentSnapshot.exists")
                        val nome = documentSnapshot.getString("nome")
                        val budget = documentSnapshot.getLong("budget")
                        val giocatoriPerSquadra = documentSnapshot.getLong("giocatoriPerSquadra")
                        val numeroGiornate = documentSnapshot.getLong("numeroGiornate")
                        // Aggiorna gli elementi UI con i valori recuperati
                        nomeEditText.setText(nome)
                        budgetEditText.setText(budget.toString())
                        giocatoriPerSquadraEditText.setText(giocatoriPerSquadra.toString())
                        numeroGiornateEditText.setText(numeroGiornate.toString())

                        val logoPath = documentSnapshot.getString("logo")
                        if (logoPath != null) {
                            // Uniforma il percorso dell'immagine
                            val fullLogoPath = if (logoPath.startsWith("/")) {
                                "gs://fantacorner-4fa20.appspot.com$logoPath"
                            } else {
                                logoPath
                            }

                            val logoUri = Uri.parse(fullLogoPath)
                            // Utilizza Glide per caricare l'immagine
                            Glide.with(this).load(logoUri).into(logoImageView)
                            Log.d("ModificaLegaActivity", "Logo caricato: $logoUri")
                        } else {
                            Log.d("ModificaLegaActivity", "Il documento non contiene un campo logo.")
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Errore nel recupero dei dettagli della lega",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Errore nel recupero dei dettagli della lega",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
        }
    }

    private fun modificaLega(
        legaId: String,
        nome: String,
        budget: Int,
        giocatoriPerSquadra: Int,
        numeroGiornate: Int
    ) {
        if (legaId != null) {
            val legaRef = FirebaseFirestore.getInstance().collection("leghe").document(legaId)

            val legaData = hashMapOf(
                "nome" to nome,
                "budget" to budget,
                "giocatoriPerSquadra" to giocatoriPerSquadra,
                "numeroGiornate" to numeroGiornate
            )

            legaRef.update(legaData as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Lega modificata con successo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Errore nella modifica della lega",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
