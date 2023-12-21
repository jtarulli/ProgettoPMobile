package com.example.progettopm.ui// com.example.progettopm.ui.CreazioneLegaActivity.kt
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
        val parolaDOrdineEditText: EditText = findViewById(R.id.parolaDOrdineEditText)
        val confermaButton: Button = findViewById(R.id.confermaButton)

        var storageRef = FirebaseStorage.getInstance()

        //upload logo
        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                logoImageView.setImageURI(it)
                uriLogo = it!!
            })

        logoButton.setOnClickListener {
            galleryImage.launch("image/*")

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

            //logo
            storageRef.getReference("loghi leghe").child(System.currentTimeMillis().toString())
                .putFile(uriLogo)
                .addOnSuccessListener {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid

                    val mapImage = mapOf(
                        "url" to it.toString()
                    )

                    val databaseReference = FirebaseDatabase.getInstance().getReference("userImages") //path da cambiare credo
                    databaseReference.child(userId).setValue(mapImage)
                        .addOnSuccessListener{
                            Toast.makeText(this, "Successufull", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{
                            error ->
                            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }
}
