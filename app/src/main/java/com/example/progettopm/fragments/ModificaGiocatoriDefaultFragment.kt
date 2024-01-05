package com.example.progettopm.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giocatore
import com.example.progettopm.view.ModificaGiocatoriAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ModificaGiocatoriDefaultFragment : Fragment(), ModificaGiocatoriAdapter.OnItemClickListener {

    private lateinit var modificaGiocatoriAdapter: ModificaGiocatoriAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_default_modifica_giocatori, container, false)
        val addButton : Button = view.findViewById(R.id.aggiungiGiocatoreButton)
        addListener(addButton, AggiungiModificaGiocatoriFragment())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewModifica)
        modificaGiocatoriAdapter = ModificaGiocatoriAdapter()

        modificaGiocatoriAdapter.itemClickListener = object : ModificaGiocatoriAdapter.OnItemClickListener {
            override fun onDelete(giocatoreId: String) {
                // Richiama la funzione onDelete dell'interfaccia OnItemClickListener
                this@ModificaGiocatoriDefaultFragment.onDelete(giocatoreId)
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = modificaGiocatoriAdapter
        }

        loadGiocatoriData()

    }

    private fun addListener(button : Button, fragment: Fragment){
        button.setOnClickListener {
            changeFragment(fragment)
        }
    }

    private fun changeFragment(fragment : Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit()
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

            giocatoriRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Gestisci l'errore
                    Log.w("ModificaGiocatoriDefaultFragment", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val giocatoriList = mutableListOf<Giocatore>()
                    for (document in snapshot.documents) {
                        val giocatore = document.toObject(Giocatore::class.java)
                        giocatoriList.add(giocatore!!)
                    }

                    // Aggiorna l'UI con la nuova lista
                    modificaGiocatoriAdapter.submitList(giocatoriList)
                } else {
                    Log.d("ModificaGiocatoriDefaultFragment", "Current data: null")
                }
            }
        }
    }

    override fun onDelete(giocatoreId: String) {
        // Implementa la logica di eliminazione del giocatore
        FirebaseFirestore.getInstance()
            .collection("giocatori")
            .document(giocatoreId)
            .delete()
            .addOnSuccessListener {
                // Aggiorna la lista dopo l'eliminazione
                loadGiocatoriData()
            }
            .addOnFailureListener {
                // Gestisci eventuali errori durante l'eliminazione
                Toast.makeText(
                    activity,
                    "Errore durante l'eliminazione del giocatore",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
