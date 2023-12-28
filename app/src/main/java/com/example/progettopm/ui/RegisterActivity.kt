package com.example.progettopm.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.example.progettopm.model.Utente
import com.example.progettopm.view.MasterActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var nomeEditText: TextInputEditText
    private lateinit var cognomeEditText: TextInputEditText
    private lateinit var logoImageView: ImageView
    private lateinit var logoButton: Button
    private lateinit var buttonReg: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var uriLogo: Uri

    // Dichiarazione del result launcher al di fuori del blocco onCreate
    private val galleryImage =
        registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
            logoImageView.setImageURI(it)
            if (it != null) {
                uriLogo = it
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        auth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.emailEditText)
        editTextPassword = findViewById(R.id.passwordEditText)
        nomeEditText = findViewById(R.id.nomeEditText)
        cognomeEditText = findViewById(R.id.cognomeEditText)
        logoImageView = findViewById(R.id.logoImageView)
        logoButton = findViewById(R.id.logoButton)
        buttonReg = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar) // Assumi che l'ID della ProgressBar sia progressBar

        logoButton.setOnClickListener {
            // Avvia la galleria per la selezione dell'immagine
            galleryImage.launch("image/*")
        }

        buttonReg.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val nome = nomeEditText.text.toString()
            val cognome = cognomeEditText.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nome) || TextUtils.isEmpty(
                    cognome
                ) || uriLogo == null
            ) {
                Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Creazione utente riuscita
                        val firebaseUser = auth.currentUser
                        val userId = firebaseUser?.uid

                        // Aggiungi l'utente al Firestore
                        if (userId != null) {
                            val utente = Utente(
                                id = userId,
                                nome = nome,
                                cognome = cognome,
                                ruolo = "user",
                                email = email,
                                password = password,
                                foto = "",  // Lascia vuota, poiché l'immagine è caricata separatamente
                                leghe = emptyList(),  // Inizializza con un elenco vuoto di leghe
                                squadre = emptyList()
                            )

                            FirebaseFirestore.getInstance().collection("utenti")
                                .document(userId)
                                .set(utente)
                                .addOnSuccessListener {
                                    // Successo nell'aggiungere l'utente al Firestore
                                    Toast.makeText(
                                        this,
                                        "Registrazione effettuata con successo",
                                        Toast.LENGTH_SHORT

                                    ).show()

                                    // Carica l'immagine del logo
                                    uploadLogoToStorage(userId)
                                }
                                .addOnFailureListener {
                                    // Errore nell'aggiungere l'utente al Firestore
                                    Toast.makeText(
                                        this,
                                        "Errore durante la registrazione",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    progressBar.visibility = View.GONE
                                }
                        }
                    } else {
                        // Errore nella creazione dell'utente
                        Toast.makeText(this, "Registrazione FALLITA", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                }
        }
    }

    private fun uploadLogoToStorage(userId: String) {
        val logoRef =
            FirebaseStorage.getInstance().reference.child("pic_utenti").child("$userId.jpg")

        logoRef.putFile(uriLogo)
            .addOnSuccessListener {
                // Aggiorna il campo 'logo' nel documento dell'utente con l'URL dell'immagine
                updateUsersLogoUrl(userId, it.metadata?.path)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Errore durante l'upload del logo", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
    }

    private fun updateUsersLogoUrl(userId: String, logoPath: String?) {
        if (logoPath != null) {
            val userRef = FirebaseFirestore.getInstance().collection("utenti").document(userId)

            // Aggiorna il campo 'logo' nel documento dell'utente con l'URL dell'immagine
            userRef.update("logo", logoPath)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "URL del logo aggiornato con successo",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, MasterActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Errore durante l'aggiornamento dell'URL del logo",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
        }
    }
}
