
package com.example.progettopm.ui

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
import com.google.android.gms.tasks.Task
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
        val view = inflater.inflate(R.layout.fragment_giocatori, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewGiocatori)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadGiocatoriData()
    }

    private fun setupRecyclerView() {
        giocatoriAdapter = GiocatoriAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = giocatoriAdapter
        }
    }

    private fun loadBonusGiornata(giocatoreId: String): Task<QuerySnapshot> {
        val legaCorrenteId = SessionManager.legaCorrenteId
        val bonusGiornataRef = FirebaseFirestore.getInstance().collection("bonus_giornata")
            .whereEqualTo("lega", FirebaseFirestore.getInstance().document("leghe/$legaCorrenteId"))
            .whereEqualTo("giocatore", giocatoreId)

        return bonusGiornataRef.get()
    }

    private fun loadGiocatoriData() {
        val legaCorrenteId = SessionManager.legaCorrenteId
        if (legaCorrenteId != null) {
            val giocatoriRef = FirebaseFirestore.getInstance().collection("giocatori")
                .whereEqualTo(
                    "lega",
                    FirebaseFirestore.getInstance().document("leghe/$legaCorrenteId")
                )
                .orderBy("punteggio", Query.Direction.DESCENDING)

            giocatoriRef.get().addOnSuccessListener { result ->
                val giocatoriList = mutableListOf<Giocatore>()
                for (document in result) {
                    val giocatore = document.toObject(Giocatore::class.java)
                    giocatoriList.add(giocatore)

                    // Carichiamo i DocumentReference dei BonusGiornata referenziati
                    val bonusGiornataRef = FirebaseFirestore.getInstance().collection("bonus_giornata")
                        .whereEqualTo("lega", FirebaseFirestore.getInstance().document("leghe/$legaCorrenteId"))
                        .whereEqualTo("giocatore", FirebaseFirestore.getInstance().document("giocatori/${giocatore.id}"))

                    bonusGiornataRef.get().addOnSuccessListener { bonusGiornataSnapshot ->
                        val bonusGiornataList =
                            bonusGiornataSnapshot.documents.map { it.reference } // manteniamo il tipo come List<DocumentReference>
                        giocatore.bonusGiornataList = bonusGiornataList

                        // Aggiorniamo la recyclerView solo quando abbiamo caricato tutti i dati
                        giocatoriAdapter.submitList(giocatoriList)
                    }
                }
            }
        }
    }

}