package com.example.progettopm.fragments.mainFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Formazione
import com.example.progettopm.view.StoricoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class StoricoFragment : Fragment() {

    private lateinit var storicoRecycleView: RecyclerView
    private lateinit var storicoAdapter: StoricoAdapter
    private val formazioniList = mutableListOf<Formazione>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_storico, container, false)
        storicoRecycleView = view.findViewById(R.id.storicoRecycleView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupero dei dati da Firestore
        caricaFormazioni()
    }

    private fun caricaFormazioni(){
        //Controllo che venga scelto lo storico dell'utente loggato e della lega corrente
        // Ottieni l'istanza dell'utente corrente
        val user = FirebaseAuth.getInstance().currentUser
        lateinit var userId: String
        // Verifica se l'utente è attualmente autenticato
        if (user != null) {
            userId = user.uid.toString()
            // Ora userId contiene l'ID dell'utente corrente
        } else {
            // L'utente non è attualmente autenticato
        }

        val legaId = SessionManager.legaCorrenteId
        val formazioniCollection = FirebaseFirestore.getInstance().collection("formazioni")

        formazioniCollection
            .whereEqualTo("id", userId)
            .whereEqualTo("legaId", legaId)
            .orderBy("giornata", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val formazione = document.toObject(Formazione::class.java)
                    formazioniList.add(formazione)
                }

                // Inizializzazione e associazione dell'adapter alla RecyclerView
                storicoAdapter = StoricoAdapter(formazioniList)
                storicoRecycleView.layoutManager = LinearLayoutManager(activity)
                storicoRecycleView.adapter = storicoAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Errore nel recupero delle formazioni: $exception", Toast.LENGTH_SHORT).show()
            }
    }
}