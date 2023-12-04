package com.example.progettopm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LegheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leghe)

        // Gestione del clic sul pulsante "UNISCITI A UNA LEGA"
        val uniscitiButton: Button = findViewById(R.id.uniscitiButton)
        uniscitiButton.setOnClickListener {
            val intent = Intent(this, ElencoLegheActivity::class.java)
            startActivity(intent)
        }

        // Gestione del clic sul pulsante "CREA LA TUA LEGA"
        val creaLegaButton: Button = findViewById(R.id.creaLegaButton)
        creaLegaButton.setOnClickListener {
            val intent = Intent(this, CreazioneLegaActivity::class.java)
            startActivity(intent)
        }
    }
}