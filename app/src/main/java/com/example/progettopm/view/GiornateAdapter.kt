package com.example.progettopm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Giornata
import java.text.SimpleDateFormat
import java.util.*

class GiornateAdapter(private var giornateList: List<Giornata>, private val listener: GiornataListener) :
    RecyclerView.Adapter<GiornateAdapter.GiornataViewHolder>() {

    interface GiornataListener {
        fun onModificaClick(position: Int)
        fun onEliminaClick(position: Int)
    }

    private val mutableGiornateList = giornateList.toMutableList()

    class GiornataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataOraInizioTextView: TextView = itemView.findViewById(R.id.dataOraInizioTextView)
        val dataOraFineTextView: TextView = itemView.findViewById(R.id.dataOraFineTextView)
        val modificaButton: Button = itemView.findViewById(R.id.modificaButton)
        val eliminaButton: Button = itemView.findViewById(R.id.eliminaButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiornataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_giornata, parent, false)
        return GiornataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GiornataViewHolder, position: Int) {
        val giornata = giornateList[position]

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val dataOraInizioText =
            "Data e Ora di Inizio: ${dateFormat.format(Date.from(giornata.inizio))} ${timeFormat.format(Date.from(giornata.inizio))}"
        val dataOraFineText =
            "Data e Ora di Fine: ${dateFormat.format(Date.from(giornata.fine))} ${timeFormat.format(Date.from(giornata.fine))}"

        holder.dataOraInizioTextView.text = dataOraInizioText
        holder.dataOraFineTextView.text = dataOraFineText

        // Aggiungi il listener per l'azione Modifica
        holder.modificaButton.setOnClickListener {
            listener.onModificaClick(position)
        }

        // Aggiungi il listener per l'azione Elimina
        holder.eliminaButton.setOnClickListener {
            listener.onEliminaClick(position)
        }
    }

    override fun getItemCount(): Int {
        return giornateList.size
    }

    // Restituisci una copia della lista per evitare modifiche indesiderate dall'esterno
    fun getGiornateList(): List<Giornata> {
        return ArrayList(mutableGiornateList)
    }

    // Aggiungi una nuova giornata alla lista
    fun aggiungiGiornata(giornata: Giornata) {
        mutableGiornateList.add(giornata)
        notifyDataSetChanged()
    }

    // Rimuovi una giornata dalla lista
    fun rimuoviGiornata(giornata: Giornata) {
        mutableGiornateList.remove(giornata)
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Giornata>) {
        giornateList = newList
        notifyDataSetChanged()
    }
}
