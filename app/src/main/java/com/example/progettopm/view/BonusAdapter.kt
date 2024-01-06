package com.example.progettopm.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.DeleteConfirmationDialog
import com.example.progettopm.DeleteConfirmationDialog.OnConfirmListener
import com.example.progettopm.databinding.ItemBonusBinding
import com.example.progettopm.model.Bonus
import com.example.progettopm.ui.CreaBonusActivity
import com.google.firebase.firestore.FirebaseFirestore


class BonusAdapter :
    ListAdapter<Bonus, BonusAdapter.BonusViewHolder>(DiffCallback()) {

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusViewHolder {
        val binding = ItemBonusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BonusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BonusViewHolder, position: Int) {
        val giocatore = getItem(position)
        holder.bind(giocatore)
    }

    inner class BonusViewHolder(private val binding: ItemBonusBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var idBonus: String
        fun bind(bonus: Bonus) {
            idBonus = bonus.id
            binding.nomeTextView.text = bonus.nome
            binding.bonusTextView.text = bonus.valore.toString()

            binding.btnEdit.setOnClickListener {
                val intent = Intent(it.context, CreaBonusActivity::class.java)
                intent.putExtra("bonus", idBonus)
                startActivity(it.context, intent, null)
            }

            binding.btnDelete.setOnClickListener {
                DeleteConfirmationDialog.showConfirmationDialog(
                    it.context,
                    "Conferma eliminazione",
                    "Sei sicuro di voler eliminare questo elemento?",
                    object : OnConfirmListener {
                        override fun onConfirm() {
                            FirebaseFirestore.getInstance()
                                .collection("bonus")
                                .document(idBonus)
                                .delete()
                            val giocatoreId = getItem(adapterPosition).id
                            itemClickListener?.onDelete(giocatoreId)
                        }
                    })
            }
        }

    }

    interface OnItemClickListener {
        fun onDelete(giocatoreId: String)
    }
    class DiffCallback : DiffUtil.ItemCallback<Bonus>() {
        override fun areItemsTheSame(oldItem: Bonus, newItem: Bonus): Boolean {
            return oldItem.id == newItem.id // Supponendo che ci sia un campo "id" o qualcosa di simile che identifica univocamente il Giocatore
        }

        override fun areContentsTheSame(oldItem: Bonus, newItem: Bonus): Boolean {
            return oldItem == newItem // Questo controlla che tutti i campi siano identici, inclusi punteggio e nome
        }
    }

}
