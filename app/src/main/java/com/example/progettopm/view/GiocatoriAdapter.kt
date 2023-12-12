package com.example.progettopm.view// com.example.progettopm.view.GiocatoriAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Giocatore

class GiocatoriAdapter : RecyclerView.Adapter<GiocatoriAdapter.GiocatoreViewHolder>() {

    private var giocatoriList: List<Giocatore> = ArrayList()

    fun setData(giocatoriList: List<Giocatore>) {
        this.giocatoriList = giocatoriList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiocatoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_giocatore, parent, false)
        return GiocatoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GiocatoreViewHolder, position: Int) {
        val giocatore = giocatoriList[position]
        holder.bind(giocatore)
    }

    override fun getItemCount(): Int {
        return giocatoriList.size
    }

    class GiocatoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNome: TextView = itemView.findViewById(R.id.textViewNome)
        private val textViewPunteggio: TextView = itemView.findViewById(R.id.textViewPunteggio)

        fun bind(giocatore: Giocatore) {
            textViewNome.text = "${giocatore.nome} ${giocatore.cognome}"
            textViewPunteggio.text = "Punteggio: ${giocatore.punteggio}"
        }
    }
}
