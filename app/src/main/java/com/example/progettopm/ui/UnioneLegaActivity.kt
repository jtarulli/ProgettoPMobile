package com.example.progettopm.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.example.progettopm.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UnioneLegaActivity : AppCompatActivity() {

    private lateinit var uriLogo: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_lega)

        val logoImageView: ImageView = findViewById(R.id.logoLegaImageView)
        val logoButton: Button = findViewById(R.id.logoButton)
        val nomeEditText: EditText = findViewById(R.id.nomeEditText)
        val budgetEditText: EditText = findViewById(R.id.budgetEditText)
        val giocatoriPerSquadraEditText: EditText = findViewById(R.id.giocatoriPerSquadraEditText)
        val numeroGiornateEditText: EditText = findViewById(R.id.numeroGiornateEditText)
        val confermaButton: Button = findViewById(R.id.confermaButton)

        val storageRef = FirebaseStorage.getInstance().reference

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
            // Gestione del clic sul pulsante di conferma

            // Recupera i valori inseriti dall'utente
            val nome = nomeEditText.text.toString()
            val budget = budgetEditText.text.toString().toIntOrNull()
            val giocatoriPerSquadra = giocatoriPerSquadraEditText.text.toString().toIntOrNull()
            val numeroGiornate = numeroGiornateEditText.text.toString().toIntOrNull()

            // Verifica che i campi obbligatori siano stati compilati
            if (nome.isEmpty() || budget == null || giocatoriPerSquadra == null || numeroGiornate == null) {
                Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Salvataggio dei dati nel database
            saveLeagueToDatabase(nome, budget, giocatoriPerSquadra, numeroGiornate)

            // Passa alla schermata successiva (HomeFragment)
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
        }
    }

    private fun saveLeagueToDatabase(
        nome: String,
        budget: Int,
        giocatoriPerSquadra: Int,
        numeroGiornate: Int
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Creazione di un nuovo documento nella raccolta 'leghe'
        val leagueDocument = FirebaseFirestore.getInstance().collection("leghe").document()

        // Creazione di un oggetto mappa con i dati della lega
        val leagueData = hashMapOf(
            "admin" to userId,
            "nome" to nome,
            "budget" to budget,
            "giocatoriPerSquadra" to giocatoriPerSquadra,
            "numeroGiornate" to numeroGiornate,
            "logo" to ""
        )

        // Aggiunta del documento lega al Firestore
        leagueDocument.set(leagueData)
            .addOnSuccessListener {
                Toast.makeText(this, "Lega creata con successo", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore durante la creazione della lega", Toast.LENGTH_SHORT)
                    .show()
            }

        // Caricamento dell'immagine del logo nel Firebase Storage
        uploadLogoToStorage(leagueDocument.id)
    }

    private fun uploadLogoToStorage(leagueId: String) {
        val logoRef = FirebaseStorage.getInstance().reference.child("loghi_leghe").child("$leagueId.jpg")

        logoRef.putFile(uriLogo)
            .addOnSuccessListener {
                // Aggiorna il campo 'logo' nel documento della lega con l'URL dell'immagine
                updateLeagueLogoUrl(leagueId, it.metadata?.path)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore durante l'upload del logo", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateLeagueLogoUrl(leagueId: String, logoPath: String?) {
        if (logoPath != null) {
            val leagueRef = FirebaseFirestore.getInstance().collection("leghe").document(leagueId)

            // Aggiorna il campo 'logo' nel documento della lega con l'URL dell'immagine
            leagueRef.update("logo", logoPath)
                .addOnSuccessListener {
                    Toast.makeText(this, "URL del logo aggiornato con successo", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Errore durante l'aggiornamento dell'URL del logo", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
}
