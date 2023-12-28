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
import com.example.progettopm.view.MasterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreazioneLegaActivity : AppCompatActivity() {

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
            // Recupera i valori inseriti dall'utente
            val nome = nomeEditText.text.toString().trim()
            val budget = budgetEditText.text.toString().trim().toIntOrNull()
            val giocatoriPerSquadra = giocatoriPerSquadraEditText.text.toString().trim().toIntOrNull()
            val numeroGiornate = numeroGiornateEditText.text.toString().trim().toIntOrNull()

            // Verifica che tutti i campi siano stati compilati
            if (nome.isEmpty() || budget == null || giocatoriPerSquadra == null || numeroGiornate == null) {
                Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Salvataggio dei dati nel database
            saveLeagueToDatabase(nome, budget, giocatoriPerSquadra, numeroGiornate)

            // Torna alla schermata principale (MasterActivity) che contiene HomeFragment
            val intent = Intent(this, MasterActivity::class.java)
            startActivity(intent)

            // Chiudi l'attività corrente
            finish()
        }
    }

    private fun saveLeagueToDatabase(
        nome: String,
        budget: Int,
        giocatoriPerSquadra: Int,
        numeroGiornate: Int
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Riferimento alla raccolta 'leghe' nel Firestore
            val legheCollection = FirebaseFirestore.getInstance().collection("leghe")

            // Creazione di un nuovo documento nella raccolta 'leghe'
            val leagueDocument = legheCollection.document()

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
                    // Aggiungi la reference alla lega appena creata nel campo 'leghe' dell'utente
                    aggiungiLegaAllUtente(userId, leagueDocument.id)
                    (this as? HomeFragment.OnDataChangeListener)?.onDataChanged()
                    Toast.makeText(this, "Lega creata con successo", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Errore durante la creazione della lega", Toast.LENGTH_SHORT)
                        .show()
                }

            // Caricamento dell'immagine del logo nel Firebase Storage
            uploadLogoToStorage(leagueDocument.id)
        } else {
            Toast.makeText(this, "Errore durante il recupero dell'utente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun aggiungiLegaAllUtente(userId: String, leagueId: String) {
        val utenteRef = FirebaseFirestore.getInstance().collection("utenti").document(userId)

        // Creazione di una reference alla lega
        val leagueRef = FirebaseFirestore.getInstance().document("leghe/$leagueId")

        // Aggiungi la reference alla lega appena creata nel campo 'leghe' dell'utente
        utenteRef.update("leghe", FieldValue.arrayUnion(leagueRef))
            .addOnSuccessListener {
                Toast.makeText(this, "Lega aggiunta all'utente con successo", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore durante l'aggiunta della lega all'utente", Toast.LENGTH_SHORT).show()
            }
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
