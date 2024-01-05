package com.example.progettopm.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.progettopm.R
import com.example.progettopm.ui.BonusActivity
import com.example.progettopm.ui.CalendarioActivity
import com.example.progettopm.ui.GiornateActivity
import com.example.progettopm.ui.ModificaGiocatoreActivity
import com.example.progettopm.ui.ModificaLegaActivity

class AdminLegaDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_lega_dashboard, container, false)

        // Imposta il titolo della toolbar
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Admin Lega Dashboard"

        // Bottone "Modifica Lega"
        val modificaLegaButton = view.findViewById<Button>(R.id.modificaLegaButton)
        modificaLegaButton.setOnClickListener {
            // Avvia l'activity di CreazioneLegaActivity in modalità di modifica
            val intent = Intent(activity, ModificaLegaActivity::class.java)
            intent.putExtra("MODIFICA_LEGA", true)
            startActivity(intent)
        }

        // Bottone "Gestione Bonus"
        val gestioneBonusButton = view.findViewById<Button>(R.id.gestioneBonusButton)
        gestioneBonusButton.setOnClickListener {
            // Avvia l'activity di GestioneBonusActivity
            val intent = Intent(activity, BonusActivity::class.java)
            startActivity(intent)
        }

        val gestioneGiocatoriButton = view.findViewById<Button>(R.id.gestioneGiocatoriButton)
        gestioneGiocatoriButton.setOnClickListener {
            val intent = Intent(activity, ModificaGiocatoreActivity::class.java)
            intent.putExtra("MODIFICA_GIOCATORI", true)
            startActivity(intent)
        }

        // Bottone "Calendario"
        val calendarioButton = view.findViewById<Button>(R.id.calendarioButton)
        calendarioButton.setOnClickListener {
            // Avvia l'activity di CalendarioActivity
            val intent = Intent(activity, CalendarioActivity::class.java)
            startActivity(intent)
        }


        return view
    }
}
