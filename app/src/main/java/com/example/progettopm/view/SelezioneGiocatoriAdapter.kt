package com.example.progettopm.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.progettopm.R
import com.example.progettopm.databinding.ItemSelezioneGiocatoreBinding
import com.example.progettopm.model.Giocatore
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SelezioneGiocatoriAdapter(
    private val giocatoriPerSquadra: Int,
    private val giocatoriRimastiTextView: TextView,
    private var budgetLega: Int,
    private val budgetRimastoTextView: TextView
) : ListAdapter<Giocatore, SelezioneGiocatoriAdapter.GiocatoreViewHolder>(DiffCallback()) {

    private val giocatoriSelezionati = mutableSetOf<String>()

    init {
        giocatoriRimastiTextView.text = giocatoriPerSquadra.toString()
        budgetRimastoTextView.text = budgetLega.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiocatoreViewHolder {
        val binding = ItemSelezioneGiocatoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GiocatoreViewHolder(binding)
    }

    fun setBudget(budget: Int) {
        budgetLega = budget
        updateBudgetRimasto()
    }

    private fun updateBudgetRimasto() {
        budgetRimastoTextView.text = "Budget rimasto: $budgetLega"
    }
    override fun onBindViewHolder(holder: GiocatoreViewHolder, position: Int) {
        val giocatore = getItem(position)
        holder.bind(giocatore)

        holder.itemView.setBackgroundColor(
            if (giocatoriSelezionati.contains(giocatore.id)) {
                ContextCompat.getColor(holder.itemView.context, R.color.selezionato)
            } else {
                Color.TRANSPARENT
            }
        )
    }

    fun getGiocatoriSelezionati(): List<DocumentReference> {
        return giocatoriSelezionati.map { giocatoreId ->
            FirebaseFirestore.getInstance().collection("giocatori").document(giocatoreId)
        }
    }

    inner class GiocatoreViewHolder(private val binding: ItemSelezioneGiocatoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(giocatore: Giocatore) {
            binding.nomeCognomeTextView.text = "${giocatore.nome} ${giocatore.cognome}"

            Glide.with(binding.root)
                .load(giocatore.foto)
                .placeholder(R.drawable.placeholder_logo)
                .into(binding.fotoImageView)

            binding.valoreTextView.text = giocatore.valore.toString()

            binding.root.setOnClickListener {
                toggleSelezione(giocatore)
            }
        }
    }

    private fun toggleSelezione(giocatore: Giocatore) {
        if (giocatoriSelezionati.contains(giocatore.id)) {
            giocatoriSelezionati.remove(giocatore.id)

            // Aggiorna il budget sottraendo il valore del giocatore deselezionato
            budgetLega += giocatore.valore
        } else {
            if (giocatoriSelezionati.size < giocatoriPerSquadra) {
                if (budgetLega - giocatore.valore >= 0) { // Controlla se il budget è sufficiente
                    giocatoriSelezionati.add(giocatore.id)

                    // Aggiorna il budget sottraendo il valore del giocatore selezionato
                    budgetLega -= giocatore.valore
                } else {
                    Toast.makeText(
                        giocatoriRimastiTextView.context,
                        "Non hai abbastanza budget per selezionare questo giocatore",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    giocatoriRimastiTextView.context,
                    "Hai già selezionato il numero massimo di giocatori",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Aggiorna i contatori
        giocatoriRimastiTextView.text = "Giocatori rimasti:${(giocatoriPerSquadra - giocatoriSelezionati.size).toString()}"
        val sommaValori = giocatoriSelezionati.sumBy { giocatoreId ->
            val index = currentList.indexOfFirst { it.id == giocatoreId }
            currentList[index].valore
        }
        budgetRimastoTextView.text = "Budget: $budgetLega"

        notifyDataSetChanged()

    }

    private class DiffCallback : DiffUtil.ItemCallback<Giocatore>() {
        override fun areItemsTheSame(oldItem: Giocatore, newItem: Giocatore): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Giocatore, newItem: Giocatore): Boolean {
            return oldItem == newItem
        }
    }
}
