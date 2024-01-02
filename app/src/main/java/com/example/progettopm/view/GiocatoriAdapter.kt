package com.example.progettopm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.progettopm.R
import com.example.progettopm.databinding.ItemGiocatoreBinding
import com.example.progettopm.model.Bonus
import com.example.progettopm.model.BonusGiornata
import com.example.progettopm.model.Giocatore
import com.google.firebase.firestore.DocumentReference

class GiocatoriAdapter :
    ListAdapter<Giocatore, GiocatoriAdapter.GiocatoreViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiocatoreViewHolder {
        val binding = ItemGiocatoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiocatoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiocatoreViewHolder, position: Int) {
        val giocatore = getItem(position)
        holder.bind(giocatore)
    }

    inner class GiocatoreViewHolder(private val binding: ItemGiocatoreBinding) : RecyclerView.ViewHolder(binding.root) {

        private val bonusAdapter = BonusGiornataAdapter()

        init {
            binding.recyclerViewBonusGiornata.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = bonusAdapter
            }
        }

        fun bind(giocatore: Giocatore) {
            binding.nomeTextView.text = giocatore.nome
            binding.cognomeTextView.text = giocatore.cognome

            Glide.with(binding.root)
                .load(giocatore.foto)
                .placeholder(R.drawable.placeholder_logo)
                .into(binding.fotoImageView)

            if (giocatore.bonusGiornataList.isNotEmpty()) {
                bonusAdapter.submitList(giocatore.bonusGiornataList)
                calculateTotalScore(giocatore.bonusGiornataList) { totalScore ->
                    binding.punteggioTextView.text = totalScore.toString()
                }
            } else {
                // Se il giocatore non ha bonus giornata, visualizza il punteggio iniziale
                binding.punteggioTextView.text = giocatore.punteggio.toString()
            }
        }

        private fun calculateTotalScore(bonusGiornataList: List<DocumentReference>, callback: (Int) -> Unit) {
            var runningTotal = 0
            var remainingCount = bonusGiornataList.size

            if (remainingCount == 0) {
                callback(runningTotal)
                return
            }

            for (bonusRef in bonusGiornataList) {
                bonusRef.get().addOnSuccessListener { bonusSnapshot ->
                    val bonusGiornata = bonusSnapshot.toObject(BonusGiornata::class.java)
                    if (bonusGiornata != null) {
                        val tipoBonusRef = bonusGiornata.tipoBonus

                        if (tipoBonusRef != null) {
                            tipoBonusRef.get().addOnSuccessListener { tipoBonusSnapshot ->
                                val tipoBonus = tipoBonusSnapshot.toObject(Bonus::class.java)
                                if (tipoBonus != null) {
                                    val bonusScore = bonusGiornata.quantita * tipoBonus.valore
                                    runningTotal += bonusScore
                                }
                                remainingCount--

                                if (remainingCount == 0) {
                                    callback(runningTotal)
                                }
                            }
                        } else {
                            remainingCount--

                            if (remainingCount == 0) {
                                callback(runningTotal)
                            }
                        }
                    } else {
                        remainingCount--

                        if (remainingCount == 0) {
                            callback(runningTotal)
                        }
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Giocatore>() {
        override fun areItemsTheSame(oldItem: Giocatore, newItem: Giocatore): Boolean {
            return oldItem.id == newItem.id // Supponendo che ci sia un campo "id" o qualcosa di simile che identifica univocamente il Giocatore
        }

        override fun areContentsTheSame(oldItem: Giocatore, newItem: Giocatore): Boolean {
            return oldItem == newItem // Questo controlla che tutti i campi siano identici, inclusi punteggio e nome
        }
    }

}
