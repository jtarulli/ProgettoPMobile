package com.example.progettopm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Giocatore

class GiocatoriAdapter : RecyclerView.Adapter<GiocatoriAdapter.ViewHolder>() {

    private var giocatori: List<Giocatore> = mutableListOf()
    private var itemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        itemClickListener = listener
    }

    fun setData(giocatoriList: List<Giocatore>) {
        giocatori = giocatoriList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_giocatore, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val giocatore = giocatori[position]
        holder.bind(giocatore)

        // Gestisci il clic sugli elementi della RecyclerView
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return giocatori.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeGiocatoreTextView: TextView = itemView.findViewById(R.id.nomeGiocatoreTextView)
        private val valoreGiocatoreTextView: TextView = itemView.findViewById(R.id.valoreGiocatoreTextView)

        fun bind(giocatore: Giocatore) {
            nomeGiocatoreTextView.text = "${giocatore.nome} ${giocatore.cognome}"
            valoreGiocatoreTextView.text = "Valore: ${giocatore.valore}"
        }
    }
}
