package com.example.progettopm.view

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Bonus

class BonusAdapter(private val listener: BonusAdapterListener) :
    RecyclerView.Adapter<BonusAdapter.BonusViewHolder>() {

    private var bonusList: List<Bonus> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bonus, parent, false)
        return BonusViewHolder(view)
    }

    override fun onBindViewHolder(holder: BonusViewHolder, position: Int) {
        val bonus = bonusList[position]
        holder.bind(bonus)
    }

    override fun getItemCount(): Int {
        return bonusList.size
    }

    fun setBonusList(bonuses: List<Bonus>) {
        bonusList = bonuses
        notifyDataSetChanged()
    }

    inner class BonusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.nomeTextView)
        private val valoreTextView: TextView = itemView.findViewById(R.id.valoreTextView)
        private val modificaButton: Button = itemView.findViewById(R.id.modificaButton)
        private val eliminaButton: Button = itemView.findViewById(R.id.eliminaButton)

        init {
            modificaButton.setOnClickListener {
                val bonus = bonusList[adapterPosition]
                listener.onModificaButtonClick(bonus)
            }

            eliminaButton.setOnClickListener {
                val bonus = bonusList[adapterPosition]
                showEliminaDialog(bonus)
            }
        }

        fun bind(bonus: Bonus) {
            nomeTextView.text = bonus.nome
            valoreTextView.text = bonus.valore.toString()
        }

        private fun showEliminaDialog(bonus: Bonus) {
            AlertDialog.Builder(itemView.context)
                .setTitle("Conferma eliminazione")
                .setMessage("Confermi l'eliminazione?")
                .setPositiveButton("Conferma") { _, _ ->
                    listener.onEliminaButtonClick(bonus)
                }
                .setNegativeButton("Annulla", null)
                .show()
        }
    }

    interface BonusAdapterListener {
        fun onModificaButtonClick(bonus: Bonus)
        fun onEliminaButtonClick(bonus: Bonus)
    }
}
