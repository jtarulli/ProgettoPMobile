package com.example.progettopm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Formazione
import com.example.progettopm.view.StoricoAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class StoricoFragment : Fragment(R.layout.fragment_storico) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var storicoAdapter: StoricoAdapter
    private lateinit var formazioniArrayList: ArrayList<Formazione>
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_storico, container, false)
        recyclerView = view.findViewById(R.id.storicoRecycleView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadGiocatoriData()
    }

    private fun setupRecyclerView() {
        storicoAdapter = StoricoAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            //adapter = StoricoAdapter
        }
    }

    private fun loadGiocatoriData() {
        val formazioniCollection = db.collection("formazioni")

        formazioniCollection.get()
            .addOnSuccessListener { documents ->
                val datiFormazioneList: ArrayList<Formazione> = ArrayList()
                for (document in documents) {
                    val nomeSquadra = document.getString("nomeSquadra").toString()
                    val giornataRef = document.get("giornata") as DocumentReference
                    val giocatoriSchieratiRef = document.get("giocatoriSchierati") as DocumentReference
                    val id = document.id
                    val punteggio = document.getString("punteggio").toString()
                    val userRef = document.get("user") as DocumentReference

                    val salvaDatiFormazione = Formazione(id, giornataRef, userRef, punteggio, nomeSquadra, giocatoriSchieratiRef)
                    datiFormazioneList.add(salvaDatiFormazione)
                }

            }
            .addOnFailureListener { exception ->
                Log.w("StoricoFragment", "Errore nel recupero dei dati", exception)
            }
    }


}