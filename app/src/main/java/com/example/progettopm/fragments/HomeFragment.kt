package com.example.progettopm.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.progettopm.R
import androidx.navigation.fragment.findNavController
import com.example.progettopm.ui.CreazioneLegaActivity
import com.example.progettopm.ui.UnioneLegaActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var pulsanteUnisciti: Button
    private lateinit var pulsanteCreaLega: Button
    private lateinit var textViewNomeLega: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pulsanteUnisciti = view.findViewById(R.id.uniscitiButton)
        pulsanteCreaLega = view.findViewById(R.id.creaLegaButton)
        textViewNomeLega = view.findViewById(R.id.nomeLegaTextView)

        // Ottieni l'ID utente corrente da Firebase Authentication
        val idUtente = FirebaseAuth.getInstance().currentUser?.uid

        // Verifica se l'utente è iscritto a una lega
        if (idUtente != null) {
            controllaAppartenenzaLegaUtente(idUtente)
        }

        // Gestisci il click sul pulsante "Unisciti a una lega"
        pulsanteUnisciti.setOnClickListener {
            // Avvia l'activity per unirsi a una lega
            val intent = Intent(requireContext(), UnioneLegaActivity::class.java)
            startActivity(intent)
        }

        // Gestisci il click sul pulsante "Crea la tua lega"
        pulsanteCreaLega.setOnClickListener {
            // Avvia l'activity per creare una lega
            val intent = Intent(requireContext(), CreazioneLegaActivity::class.java)
            startActivity(intent)
        }
class HomeFragment : Fragment() {
    // Al momento della creazione del fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla il layout per questo fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Trova il bottone nel layout
        val inserisciFormazioneButton: Button = view.findViewById(R.id.inserisciFormazione_btn)
        val navController = findNavController()
        inserisciFormazioneButton.setOnClickListener {
            // Naviga verso il FormazioneFragment
            findNavController().navigate(R.id.formazioneFragment)
        }

        // Restituisci la vista
        return view
    }

    private fun controllaAppartenenzaLegaUtente(idUtente: String) {
        // Ottieni il riferimento al documento dell'utente nel Firestore
        val riferimentoDocUtente = FirebaseFirestore.getInstance().collection("utenti").document(idUtente)

        // Leggi i dati dell'utente dal Firestore
        riferimentoDocUtente.get()
            .addOnSuccessListener { documentoUtente ->
                if (documentoUtente.exists()) {
                    // Verifica se l'utente è iscritto a una lega
                    val leghe = documentoUtente.get("leghe") as? List<String>
                    if (leghe != null && leghe.isNotEmpty()) {
                        // L'utente è iscritto a almeno una lega
                        val nomeLega = leghe[0] // Supponendo che l'utente possa appartenere solo a una lega per ora
                        visualizzaHomeLega(nomeLega)
                    } else {
                        // L'utente non è iscritto a nessuna lega
                        visualizzaHomeNoLega()
                    }
                }
            }
            .addOnFailureListener { eccezione ->
                // Gestisci eventuali errori durante la lettura dei dati dal Firestore
                Toast.makeText(requireContext(), "Errore durante il recupero dei dati dell'utente", Toast.LENGTH_SHORT).show()
            }
    }

    private fun visualizzaHomeLega(nomeLega: String) {
        // Nascondi i pulsanti di "Unisciti a una lega" e "Crea la tua lega"
        pulsanteUnisciti.visibility = View.GONE
        pulsanteCreaLega.visibility = View.GONE

        // Mostra il nome della lega in alto
        textViewNomeLega.text = nomeLega
        textViewNomeLega.visibility = View.VISIBLE
    }

    private fun visualizzaHomeNoLega() {
        // Mostra i pulsanti di "Unisciti a una lega" e "Crea la tua lega"

        pulsanteUnisciti.visibility = View.VISIBLE
        pulsanteCreaLega.visibility = View.VISIBLE

        // Nascondi il nome della lega
        textViewNomeLega.visibility = View.GONE
    }
}
