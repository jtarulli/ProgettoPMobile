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

        // Modifica per gestire il valore null di legaCorrenteId
        val legaId = SessionManager.legaCorrenteId ?: ""

        // Ottenere la lista delle giornate per la lega corrente dal database
        val giornateList = getGiornateList(legaId)

        giornateAdapter = GiornateAdapter(giornateList, this) // Pass 'this' as the listener
        recyclerViewGiornate.layoutManager = LinearLayoutManager(activity)
        recyclerViewGiornate.adapter = giornateAdapter

        return view
    }

    private fun getGiornateList(legaId: String): List<Giornata> {
        // Implementa la logica per ottenere la lista di giornate dalla tua sorgente dati
        // In questo esempio, creiamo alcune giornate di esempio
        return listOf(
            Giornata(1, Instant.now(), Instant.now().plusSeconds(3600), legaId),
            Giornata(2, Instant.now().plusSeconds(7200), Instant.now().plusSeconds(10800), legaId)
            // Aggiungi altre giornate secondo le tue esigenze
        )
    }

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
