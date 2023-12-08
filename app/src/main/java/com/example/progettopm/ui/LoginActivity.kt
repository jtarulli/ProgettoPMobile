package com.example.progettopm.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        // Inizializza Firebase Authentication
        auth = FirebaseAuth.getInstance()

        lateinit var usernameEditText: EditText
        lateinit var passwordEditText: EditText
        var loginButton: Button = findViewById(R.id.loginButton)
        // Evento click sul pulsante di login
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Effettua l'autenticazione con username e password
            auth.signInWithEmailAndPassword("$username@yourdomain.com", password)
                .addOnCompleteListener(this) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        // Login riuscito
                        val user = auth.currentUser
                        // Avvia l'attività successiva o esegui altre azioni
                    } else {
                        // Login fallito, mostra un messaggio di errore
                        Toast.makeText(this, "Autenticazione fallita", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
