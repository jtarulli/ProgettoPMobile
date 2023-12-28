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
import com.example.progettopm.SessionManager
import com.example.progettopm.fragments.HomeFragment
import com.example.progettopm.view.MasterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreazioneSquadraActivity : AppCompatActivity() {

    private lateinit var uriLogo: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_squadra)

        val logoImageView: ImageView = findViewById(R.id.logoSquadraImageView)
        val logoButton: Button = findViewById(R.id.logoSquadraButton)
        val nomeEditText: EditText = findViewById(R.id.squadraNomeEditText)
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

            // Verifica che tutti i campi siano stati compilati
            if (nome.isEmpty()) {
                Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Salvataggio dei dati nel database
            saveLeagueToDatabase(nome)

            // Torna alla schermata principale (MasterActivity) che contiene HomeFragment
            val intent = Intent(this, MasterActivity::class.java)
            startActivity(intent)

            // Chiudi l'attività corrente
            finish()
        }
    }

    private fun saveLeagueToDatabase(nome: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val legaCorrenteId = SessionManager.legaCorrenteId  // Recupera l'ID della lega corrente

        if (userId != null && legaCorrenteId != null) {
            // Riferimento alla raccolta 'squadre' nel Firestore
            val squadreCollection = FirebaseFirestore.getInstance().collection("squadre")

            // Creazione di un nuovo documento nella raccolta 'squadre'
            val squadraDocument = squadreCollection.document()

            // Creazione di un oggetto mappa con i dati della squadra
            val squadraData = hashMapOf(
                "admin" to userId,
                "nome" to nome,
                "lega" to legaCorrenteId,
                "punteggio" to 0,
                "logo" to ""
            )

            // Aggiunta del documento lega al Firestore
            squadraDocument.set(squadraData)
                .addOnSuccessListener {
                    aggiungiSquadraAllUtente(userId, squadraDocument.id)
                    Toast.makeText(this, "Squadra creata con successo", Toast.LENGTH_SHORT).show()
                    (this as? HomeFragment.OnDataChangeListener)?.onDataChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Errore durante la creazione della squadra", Toast.LENGTH_SHORT)
                        .show()
                }

            // Caricamento dell'immagine del logo nel Firebase Storage
            uploadLogoToStorage(squadraDocument.id)
        } else {
            Toast.makeText(this, "Errore durante il recupero dell'utente o dell'ID della lega", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadLogoToStorage(squadraId: String) {
        val logoRef = FirebaseStorage.getInstance().reference.child("loghi_squadre").child("$squadraId.jpg")

        logoRef.putFile(uriLogo)
            .addOnSuccessListener {
                // L'immagine è stata caricata con successo, ora possiamo ottenere l'URL
                logoRef.downloadUrl.addOnSuccessListener { uri ->
                    // Aggiorna il campo 'logo' nel documento della squadra con l'URL dell'immagine
                    updateSquadraLogoUrl(squadraId, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore durante l'upload del logo", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateSquadraLogoUrl(squadraId: String, logoUrl: String) {
        val squadraRef = FirebaseFirestore.getInstance().collection("squadre").document(squadraId)

        // Aggiorna il campo 'logo' nel documento della squadra con l'URL dell'immagine
        squadraRef.update("logo", logoUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "URL del logo aggiornato con successo", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore durante l'aggiornamento dell'URL del logo", Toast.LENGTH_SHORT).show()
            }
    }

    private fun aggiungiSquadraAllUtente(userId: String, squadraId: String) {
        val utenteRef = FirebaseFirestore.getInstance().collection("utenti").document(userId)

        // Creazione di una reference alla squadra
        val squadraRef = FirebaseFirestore.getInstance().document("squadre/$squadraId")

        // Aggiungi la reference alla squadra appena creata nel campo 'squadre' dell'utente
        utenteRef.update("squadre", FieldValue.arrayUnion(squadraRef))
            .addOnSuccessListener {
                Toast.makeText(this, "Squadra aggiunta all'utente con successo", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore durante l'aggiunta della squadra all'utente", Toast.LENGTH_SHORT).show()
            }
    }


}
