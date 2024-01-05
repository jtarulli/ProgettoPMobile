package com.example.progettopm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giornata
import com.example.progettopm.view.GiornateAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant

class CalendarioFragment : Fragment(), GiornateAdapter.GiornataListener {

    private lateinit var recyclerViewGiornate: RecyclerView
    private lateinit var giornateAdapter: GiornateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendario, container, false)

        recyclerViewGiornate = view.findViewById(R.id.recyclerViewGiornate)

        val legaId = SessionManager.legaCorrenteId ?: ""

        setupRecyclerView(legaId)

        return view
    }

    private fun setupRecyclerView(legaId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val giornateList = getGiornateList(legaId)
            giornateAdapter = GiornateAdapter(giornateList, this@CalendarioFragment)

            recyclerViewGiornate.layoutManager = LinearLayoutManager(activity)
            recyclerViewGiornate.adapter = giornateAdapter
        }
    }

    private suspend fun getGiornateList(legaId: String): List<Giornata> {
        val firestore = FirebaseFirestore.getInstance()

        val documents = firestore.collection("giornate")
            .whereEqualTo("lega", legaId)
            .get()
            .await()

        val giornateList = mutableListOf<Giornata>()
        for (document in documents) {
            val id = document.getLong("id")?.toInt() ?: 0
            val inizio = document.getDate("inizio")?.toInstant() ?: Instant.now()
            val fine = document.getDate("fine")?.toInstant() ?: Instant.now()
            val lega = document.getString("lega") ?: ""

            val giornata = Giornata(id, inizio, fine, lega)
            giornateList.add(giornata)
        }

        return giornateList
    }

    // Implementazione delle azioni Modifica ed Elimina
    override fun onModificaClick(position: Int) {
        // Implementa l'azione di modifica qui
        Toast.makeText(requireContext(), "Modifica giornata alla posizione $position", Toast.LENGTH_SHORT).show()
    }

    override fun onEliminaClick(position: Int) {
        val giornataDaEliminare = giornateAdapter.getGiornateList()[position]

        // Mostra una finestra di dialogo per la conferma dell'eliminazione
        AlertDialog.Builder(requireContext())
            .setTitle("Conferma Eliminazione")
            .setMessage("Sei sicuro di voler eliminare questa giornata?")
            .setPositiveButton("Elimina") { dialog, _ ->
                // Elimina l'elemento dalla lista e aggiorna l'adapter
                giornateAdapter.rimuoviGiornata(giornataDaEliminare)

                // Aggiorna la lista dopo l'eliminazione
                // giornateAdapter.updateList(nuovaListaDopoEliminazione)

                Toast.makeText(requireContext(), "Giornata eliminata con successo", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
