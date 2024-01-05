package com.example.progettopm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Formazione
import com.example.progettopm.model.Squadra

class StoricoAdapter(private val formazioni: List<Formazione>) : RecyclerView.Adapter<StoricoAdapter.StoricoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoricoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_storico, parent, false)
        return StoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoricoViewHolder, position: Int) {
        val formazione = formazioni[position]
        holder.bind(formazione)
    }

    override fun getItemCount(): Int {
        return formazioni.size
    }

     class StoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(formazione: Formazione) {
            itemView.findViewById<TextView>(R.id.numGiornata_RV).text = formazione.giornata.toString()
            // Assicurati che formazione.squadra contenga il nome della squadra
            itemView.findViewById<TextView>(R.id.nomeSquadra_RV).text = formazione.squadra.toString() // Assuming formazione.squadra contains the team name
            itemView.findViewById<TextView>(R.id.punteggioSquadra_RV).text = formazione.punteggio.toString()
        }
    }
}