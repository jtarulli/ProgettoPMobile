package com.example.progettopm
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*  // Assicurati di avere un file di layout con nome activity_home.xml che contiene le icone del footer

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Assicurati di avere un layout con nome activity_home.xml

        // Gestione del click sul pulsante Home
        buttonHome.setOnClickListener {
            // Avvia l'Activity corrispondente alla schermata Home
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        // Gestione del click sul pulsante Storico
        buttonStorico.setOnClickListener {
            // Avvia l'Activity corrispondente alla schermata dello Storico
            val intent = Intent(this, StoricoActivity::class.java)
            startActivity(intent)
        }

        // Gestione del click sul pulsante Giocatori
        buttonGiocatori.setOnClickListener {
            // Avvia l'Activity corrispondente alla schermata dei Giocatori
            val intent = Intent(this, GiocatoriActivity::class.java)
            startActivity(intent)
        }

        // Gestione del click sul pulsante Classifica
        buttonClassifica.setOnClickListener {
            // Avvia l'Activity corrispondente alla schermata della Classifica
            val intent = Intent(this, ClassificaActivity::class.java)
            startActivity(intent)
        }
    }
}