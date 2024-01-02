package com.example.progettopm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Squadra

class ClassificaAdapter(private val squadre: List<Squadra>) : RecyclerView.Adapter<ClassificaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_classifica, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val squadra = squadre[position]
        holder.textPosizione.text = (position + 1).toString()
        holder.textNomeSquadra.text = squadra.nome
        holder.textPunteggio.text = squadra.punteggio.toString()
    }

    override fun getItemCount(): Int {
        return squadre.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textPosizione: TextView = itemView.findViewById(R.id.textPosizione)
        val textNomeSquadra: TextView = itemView.findViewById(R.id.textNomeSquadra)
        val textPunteggio: TextView = itemView.findViewById(R.id.textPunteggio)
    }
}