// GiornateAdapter.kt
package com.example.progettopm.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.databinding.ItemGiornataBinding
import com.example.progettopm.model.Giornata
import java.util.Calendar

class GiornataAdapter : ListAdapter<Giornata, GiornataAdapter.GiornataViewHolder>(DiffCallback()) {

    var itemClickListener: OnItemClickListener? = null
    lateinit var ctx: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiornataViewHolder {
        ctx = parent.context
        val binding = ItemGiornataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiornataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiornataViewHolder, position: Int) {
        val giornata = getItem(position)
        holder.bind(giornata)
    }

    inner class GiornataViewHolder(private val binding: ItemGiornataBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var idGiornata: String
        fun bind(giornata: Giornata) {
            idGiornata = giornata.id.toString()
            binding.dataOraInizioTextView.text = giornata.inizio.toString()
            binding.dataOraFineTextView.text = giornata.fine.toString()

            binding.modificaButton.setOnClickListener {
                mostraSceltaDate()
            }

//            binding.btnDelete.setOnClickListener {
//                DeleteConfirmationDialog.showConfirmationDialog(
//                    it.context,
//                    "Conferma eliminazione",
//                    "Sei sicuro di voler eliminare questa giornata?",
//                    object : OnConfirmListener {
//                        override fun onConfirm() {
//                            val giornataId = getItem(adapterPosition).id
//                            itemClickListener?.onDelete(giornataId)
//                        }
//                    })
//            }
        }
    }

    private fun mostraSceltaDate() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            ctx,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                mostraSceltaOraInizio(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun mostraSceltaOraInizio(calendar: Calendar) {
        val timePickerDialog = TimePickerDialog(
            ctx,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val inizio = calendar.timeInMillis
                mostraSceltaOraFine(inizio)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun mostraSceltaOraFine(inizio: Long) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            ctx,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val fine = calendar.timeInMillis

                if (fine > inizio) {
                    // Creare una nuova giornata solo se la fine è successiva all'inizio
                    //val nuovaGiornata = Giornata(getProssimoNumeroGiornata(), inizio, fine)
                    //giornateAdapter.aggiungiGiornata(nuovaGiornata)
                } else {
                    // Visualizza un messaggio di errore
                    // Implementa il feedback utente a tuo piacimento
                    // Ad esempio, mostra un Toast
                    Toast.makeText(ctx, "La data di fine deve essere successiva a quella di inizio", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    interface OnItemClickListener {
        fun onDelete(giornataId: String)
    }

    class DiffCallback : DiffUtil.ItemCallback<Giornata>() {
        override fun areItemsTheSame(oldItem: Giornata, newItem: Giornata): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Giornata, newItem: Giornata): Boolean {
            return oldItem == newItem
        }
    }
}