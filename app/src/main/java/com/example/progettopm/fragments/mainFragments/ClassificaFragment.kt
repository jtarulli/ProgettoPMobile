package com.example.progettopm.fragments.mainFragments

import android.content.ContentValues
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
import com.example.progettopm.model.Squadra
import com.example.progettopm.view.ClassificaAdapter
import com.google.firebase.firestore.FirebaseFirestore

class ClassificaFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var classificaAdapter: ClassificaAdapter
    private lateinit var squadre: List<Squadra>  // Lista di tipo Squadra

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_classifica, container, false)

        // Inizializza la RecyclerView
        recyclerView = view.findViewById(R.id.classificaRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Recupera i dati da Firestore e imposta l'adapter
        // Supponiamo di avere un'istanza di FirebaseFirestore chiamata "db"
        db = FirebaseFirestore.getInstance()
        val legaCorrente = SessionManager.legaCorrenteId
        // Recupera i dati da Firestore e gestisci la deserializzazione
        val squadre = mutableListOf<Squadra>()
        val squadreRef = db.collection("squadre").whereEqualTo("lega", legaCorrente)

        squadreRef.get()
            .addOnSuccessListener { result ->
                val squadre = result.toObjects(Squadra::class.java)
                    .sortedByDescending { it.punteggio }  // Ordina le squadre in base al punteggio in ordine decrescente
                classificaAdapter = ClassificaAdapter(squadre)
                recyclerView.adapter = classificaAdapter
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Errore recupero dati: $exception")
            }

        return view
    }
}