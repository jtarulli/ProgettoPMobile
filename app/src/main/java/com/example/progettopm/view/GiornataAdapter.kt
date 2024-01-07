// GiornateAdapter.kt
package com.example.progettopm.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.MyDatePicker
import com.example.progettopm.DeleteConfirmationDialog
import com.example.progettopm.databinding.ItemGiornataBinding
import com.example.progettopm.model.Giornata
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


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
        private lateinit var giornata: Giornata
        lateinit var inizioPicker: MyDatePicker
        lateinit var finePicker: MyDatePicker
        private lateinit var inizioDate: Timestamp
        private lateinit var fineDate: Timestamp

        fun bind(gior: Giornata) {
            giornata = gior
            val idGiornata = giornata.id
            val inizio = Instant.ofEpochSecond(giornata.inizio.seconds)
            val fine = Instant.ofEpochSecond(giornata.fine.seconds)

            setDate(inizio, fine)

            inizioPicker = MyDatePicker(ctx)
            inizioPicker.setOnDateChangedListener {
                inizioDate = Timestamp(inizioPicker.getTimeLocal().toEpochSecond(ZoneOffset.UTC), 0)
            }
            finePicker = MyDatePicker(ctx)
            finePicker.setOnDateChangedListener {
                fineDate = Timestamp(finePicker.getTimeLocal().toEpochSecond(ZoneOffset.UTC), 0)
                pushMod()
            }

            binding.modificaButton.setOnClickListener {
                finePicker.mostraSceltaDate(fine)
                inizioPicker.mostraSceltaDate(inizio)
            }

            binding.eliminaButton.setOnClickListener {
                DeleteConfirmationDialog.showConfirmationDialog(
                    it.context,
                    "Conferma eliminazione",
                    "Sei sicuro di voler eliminare questa giornata?",
                    object : DeleteConfirmationDialog.OnConfirmListener {
                        override fun onConfirm() {
                            val giornataId = getItem(adapterPosition).id
                            itemClickListener?.onDelete(giornataId)
                            FirebaseFirestore.getInstance()
                                .collection("giornate")
                                .document(idGiornata)
                                .delete()
                        }
                    })
            }
        }

        private fun setDate(initDate: Instant, finalDate: Instant){
            binding.dataOraInizioTextView.text = formatDate(initDate.toString())
            binding.dataOraFineTextView.text = formatDate(finalDate.toString())
        }

        private fun formatDate(date : String) : String{
            val inst_parse: Instant = Instant.parse(date)
            val localDateTime : LocalDateTime = parseToDt(inst_parse)
            val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")
            val parsed_date : String = formatter.format(localDateTime)
            return parsed_date
        }

        private fun parseToDt(data : Instant) : LocalDateTime {
            return LocalDateTime.ofInstant(data, ZoneId.of("GMT+1"))
        }

        private fun pushMod() {
            if (fineDate > inizioDate) {
                val updateGiornata = Giornata(
                    id = giornata.id,
                    inizio = inizioDate,
                    fine = fineDate,
                    lega = giornata.lega
                    )
                FirebaseFirestore.getInstance().collection("giornate")
                                               .document(giornata.id)
                                               .set(updateGiornata)
            } else {
                // Visualizza un messaggio di errore
                // Implementa il feedback utente a tuo piacimento
                // Ad esempio, mostra un Toast
                Toast.makeText(ctx, "La data di fine deve essere successiva a quella di inizio", Toast.LENGTH_SHORT).show()
            }
        }
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