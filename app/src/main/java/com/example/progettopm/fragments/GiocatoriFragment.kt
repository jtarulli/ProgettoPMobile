package com.example.progettopm.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giocatore
import com.example.progettopm.view.GiocatoriAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class GiocatoriFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var giocatoriAdapter: GiocatoriAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("GiocatoriFragment", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_giocatori, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewGiocatori)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("GiocatoriFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadGiocatoriData()
    }

    private fun setupRecyclerView() {
        Log.d("GiocatoriFragment", "setupRecyclerView")
        giocatoriAdapter = GiocatoriAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = giocatoriAdapter
    }

    private fun loadBonusGiornata(giocatoreId: String): Query {
        val legaCorrenteId = SessionManager.legaCorrenteId
        return FirebaseFirestore.getInstance().collection("bonus_giornata")
            .whereEqualTo("lega", FirebaseFirestore.getInstance().document("leghe/$legaCorrenteId"))
            .whereEqualTo("giocatore", giocatoreId)
    }

    private fun loadGiocatoriData() {
        Log.d("GiocatoriFragment", "loadGiocatoriData")
        val legaCorrenteId = SessionManager.legaCorrenteId
        Log.d("GiocatoriFragment", "loadGiocatoriData id lega $legaCorrenteId")
        if (legaCorrenteId != null) {
            val giocatoriRef = FirebaseFirestore.getInstance().collection("giocatori")
                .whereEqualTo("lega", FirebaseFirestore.getInstance().document("leghe/$legaCorrenteId"))
                .orderBy("punteggio", Query.Direction.DESCENDING)

            giocatoriRef.get().addOnSuccessListener { result ->
                val giocatoriList = mutableListOf<Giocatore>()

                for (document in result) {
                    val giocatore = document.toObject(Giocatore::class.java)
                    giocatoriList.add(giocatore)

                    // Carica i DocumentReference dei BonusGiornata referenziati
                    val bonusGiornataRef = loadBonusGiornata(giocatore.id)

                    bonusGiornataRef.get().addOnSuccessListener { bonusGiornataSnapshot ->
                        val bonusGiornataList = bonusGiornataSnapshot.documents.map { it.reference }
                        giocatore.bonusGiornataList = bonusGiornataList

                        // Aggiorna la recyclerView solo quando sono stati caricati tutti i dati
                        giocatoriAdapter.submitList(giocatoriList)
                    }
                }
            }
        }
    }
}
