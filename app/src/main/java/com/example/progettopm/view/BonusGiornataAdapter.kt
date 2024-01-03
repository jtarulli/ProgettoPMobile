package com.example.progettopm.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.databinding.ItemBonusGiornataBinding
import com.example.progettopm.model.Bonus
import com.example.progettopm.model.BonusGiornata
import com.google.firebase.firestore.DocumentReference

class BonusGiornataAdapter : ListAdapter<DocumentReference, BonusGiornataAdapter.BonusGiornataViewHolder>(DiffCallback()) {

    inner class BonusGiornataViewHolder(private val binding: ItemBonusGiornataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bonusGiornata: BonusGiornata) {
            val tipoBonusRef = bonusGiornata.tipoBonus
            if (tipoBonusRef != null) {
                Log.d("BonusGiornataAdapter", "TipoBonusRef is not null")
                tipoBonusRef.get().addOnSuccessListener { tipoBonusSnapshot ->
                    val tipoBonus = tipoBonusSnapshot.toObject(Bonus::class.java)
                    if (tipoBonus != null) {
                        Log.d("BonusGiornataAdapter", "TipoBonus is not null")
                        binding.nomeTipoBonusTextView.text = tipoBonus.nome ?: "Tipo Bonus non disponibile"
                        binding.quantitaTextView.text = bonusGiornata.quantita.toString()
                        binding.punteggioBonusTextView.text = calculateBonusScore(bonusGiornata, tipoBonus).toString()
                    } else {
                        // Gestisci il caso in cui non riesci a ottenere l'oggetto Bonus
                        Log.d("BonusGiornataAdapter", "TipoBonus is null")
                        binding.nomeTipoBonusTextView.text = "Tipo Bonus non disponibile"
                    }
                }
            } else {
                Log.d("BonusGiornataAdapter", "TipoBonusRef is null")
            }
        }

        private fun calculateBonusScore(bonusGiornata: BonusGiornata, tipoBonus: Bonus): Int {
            return bonusGiornata.quantita * tipoBonus.valore
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusGiornataViewHolder {
        val binding = ItemBonusGiornataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BonusGiornataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BonusGiornataViewHolder, position: Int) {
        val bonusGiornataRef = getItem(position)
        bonusGiornataRef.get().addOnSuccessListener { bonusSnapshot ->
            val bonusGiornata = bonusSnapshot.toObject(BonusGiornata::class.java)
            if (bonusGiornata != null) {
                Log.d("BonusGiornataAdapter", "Binding BonusGiornata")
                holder.bind(bonusGiornata)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DocumentReference>() {
        override fun areItemsTheSame(oldItem: DocumentReference, newItem: DocumentReference): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DocumentReference, newItem: DocumentReference): Boolean {
            // Considera se i contenuti sono gli stessi (non implementato qui)
            return true
        }
    }
}
