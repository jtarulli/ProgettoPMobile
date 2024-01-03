package com.example.progettopm.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
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

class SelezioneGiocatoriAdapter(private val giocatoriPerSquadra: Int) :
    ListAdapter<Giocatore, SelezioneGiocatoriAdapter.GiocatoreViewHolder>(DiffCallback()) {

    // Aggiungi un array di booleani per tenere traccia degli ID dei giocatori selezionati
    private val giocatoriSelezionati = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiocatoreViewHolder {
        val binding = ItemSelezioneGiocatoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GiocatoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiocatoreViewHolder, position: Int) {
        val giocatore = getItem(position)
        holder.bind(giocatore)

        // Imposta il colore dello sfondo in base alla selezione
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

            // Aggiungi logica per la selezione del giocatore
            binding.root.setOnClickListener {
                toggleSelezione(giocatore.id)
            }
        }
    }

    private fun toggleSelezione(giocatoreId: String) {
        // Inverti lo stato di selezione quando l'elemento viene cliccato
        if (giocatoriSelezionati.contains(giocatoreId)) {
            giocatoriSelezionati.remove(giocatoreId)
        } else {
            // Limita il numero di giocatori selezionati alla variabile giocatoriPerSquadra
            if (giocatoriSelezionati.size < giocatoriPerSquadra) {
                giocatoriSelezionati.add(giocatoreId)
            } else {
                // Puoi aggiungere un avviso o una logica personalizzata se il limite viene superato
            }
        }
        notifyDataSetChanged() // Aggiorna l'adapter per riflettere i cambiamenti
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
