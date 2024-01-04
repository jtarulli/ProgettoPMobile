package com.example.progettopm.fragments.mainFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.ui.CreazioneLegaActivity
import com.example.progettopm.ui.CreazioneSquadraActivity
import com.example.progettopm.ui.FormazioneActivity
import com.example.progettopm.ui.UnioneLegaActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.annotations.Nullable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var pulsanteUnisciti: Button
    private lateinit var pulsanteCreaLega: Button
    private lateinit var pulsanteCreaSquadra: Button
    private lateinit var textViewNomeLega: TextView
    private lateinit var textViewNomeSquadra: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Ottieni l'ID utente corrente da Firebase Authentication
        val idUtente = FirebaseAuth.getInstance().currentUser?.uid

        // Verifica se l'utente è iscritto a una lega
        if (idUtente != null) {
            controllaAppartenenzaLegaUtente(idUtente)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pulsanteUnisciti = view.findViewById(R.id.uniscitiButton)
        pulsanteCreaLega = view.findViewById(R.id.creaLegaButton)
        pulsanteCreaSquadra = view.findViewById(R.id.creaSquadraButton)
        textViewNomeLega = view.findViewById(R.id.nomeLegaTextView)
        textViewNomeSquadra = view.findViewById(R.id.nomeSquadraTextView)

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
        pulsanteCreaSquadra.setOnClickListener {
            // Avvia l'activity per unirsi a una lega
            val intent = Intent(requireContext(), CreazioneSquadraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun controllaAppartenenzaLegaUtente(idUtente: String) {
        // Ottieni il riferimento al documento dell'utente nel Firestore
        val utenteRef = FirebaseFirestore.getInstance().collection("utenti").document(idUtente)

        // Leggi i dati dell'utente dal Firestore
        utenteRef.get().addOnSuccessListener { getUserInformation(it) }
            .addOnFailureListener { eccezione ->
                // Gestisci eventuali errori durante la lettura dei dati dal Firestore
                Toast.makeText(
                    requireContext(),
                    "Errore durante il recupero dei dati dell'utente",
                    Toast.LENGTH_SHORT
                ).show()
            }

        utenteRef.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(
                @Nullable snapshot: DocumentSnapshot?,
                @Nullable e: FirebaseFirestoreException?
            ) {
                if (e != null) {
                    // Gestisci l'errore, se necessario
                    return
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d("Snapshot_live_data", "Test")
                    getUserInformation(snapshot)
                }
            }
        });
    }

    private fun getUserInformation(documentoUtente: DocumentSnapshot){
        if (documentoUtente.exists()) {
            // Ottieni la reference alla lega corrente
            val leghe = documentoUtente.get("leghe") as? List<DocumentReference>

            if (leghe != null && leghe.isNotEmpty()) {
                val nomeLegaRef = leghe[0]
                val nomeLegaDocId = nomeLegaRef.id
                SessionManager.legaCorrenteId = leghe[0].id

                // Ottieni il nome della lega dal riferimento del documento
                FirebaseFirestore.getInstance().collection("leghe").document(nomeLegaDocId).get()
                    .addOnSuccessListener { legaDoc ->
                        if (legaDoc.exists()) {
                            val nomeLega = legaDoc.getString("nome")

                            if (nomeLega != null) {
                                // Verifica se l'utente ha una squadra nella lega corrente
                                val squadre = documentoUtente.get("squadre") as? List<DocumentReference>

                                if (squadre != null && squadre.isNotEmpty()) {
                                    // L'utente ha almeno una squadra
                                    val nomeSquadraRef = squadre[0]
                                    SessionManager.squadraCorrenteId = squadre[0].id

                                    nomeSquadraRef.get().addOnSuccessListener { squadraDoc ->
                                        if (squadraDoc.exists()) {
                                            val nomeSquadra = squadraDoc.getString("nome")
                                            if (nomeSquadra != null) {
                                                // L'utente ha creato una squadra
                                                visualizzaHomeSquadra(nomeLega, nomeSquadra)
                                            }
                                        }
                                    }
                                } else {
                                    // L'utente non ha ancora creato una squadra, mostra il pulsante "Crea Squadra"
                                    visualizzaHomeCreaSquadra(nomeLega)
                                }
                            }
                        }
                    }
                    .addOnFailureListener { eccezione ->
                        // Gestisci eventuali errori durante la lettura del documento della lega
                        Toast.makeText(
                            requireContext(),
                            "Errore durante il recupero dei dati della lega",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                // L'utente non è iscritto a nessuna lega
                visualizzaHomeNoLega()
            }
        }
    }

    private fun visualizzaHomeNoLega() {
        pulsanteUnisciti.visibility = View.VISIBLE
        pulsanteCreaLega.visibility = View.VISIBLE
        pulsanteCreaSquadra.visibility = View.GONE
        textViewNomeLega.visibility = View.GONE
        textViewNomeSquadra.visibility = View.GONE
    }

    private fun visualizzaHomeCreaSquadra(nomeLega: String) {
        pulsanteUnisciti.visibility = View.GONE
        pulsanteCreaLega.visibility = View.GONE
        pulsanteCreaSquadra.visibility = View.VISIBLE
        textViewNomeLega.text = nomeLega
        textViewNomeLega.visibility = View.VISIBLE
        textViewNomeSquadra.visibility = View.GONE
    }

    private fun visualizzaHomeSquadra(nomeLega: String, nomeSquadra: String) {
        pulsanteUnisciti.visibility = View.GONE
        pulsanteCreaLega.visibility = View.GONE
        pulsanteCreaSquadra.visibility = View.GONE
        textViewNomeLega.text = nomeLega
        textViewNomeSquadra.text = nomeSquadra
        textViewNomeLega.visibility = View.VISIBLE
        textViewNomeSquadra.visibility = View.VISIBLE

        val inserisciFormazioneButton = view?.findViewById<Button>(R.id.inserisciFormazioneButton)
        inserisciFormazioneButton?.visibility = View.VISIBLE

        // Aggiungi il listener per gestire il click sul bottone "Inserisci Formazione"
        inserisciFormazioneButton?.setOnClickListener {
            // Avvia l'activity per inserire la formazione
            val intent = Intent(requireContext(), FormazioneActivity::class.java)
            startActivity(intent)
        }
    }
}