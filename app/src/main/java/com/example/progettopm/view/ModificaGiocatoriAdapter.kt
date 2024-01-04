package com.example.progettopm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.progettopm.DeleteConfirmationDialog
import com.example.progettopm.R
import com.example.progettopm.databinding.ItemGiocatoreModificaBinding
import com.example.progettopm.model.Giocatore
import com.example.progettopm.DeleteConfirmationDialog.OnConfirmListener
import com.example.progettopm.fragments.AggiungiModificaGiocatoriFragment
import com.google.firebase.firestore.FirebaseFirestore


class ModificaGiocatoriAdapter :
    ListAdapter<Giocatore, ModificaGiocatoriAdapter.GiocatoreViewHolder>(DiffCallback()) {

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiocatoreViewHolder {
        val binding = ItemGiocatoreModificaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiocatoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiocatoreViewHolder, position: Int) {
        val giocatore = getItem(position)
        holder.bind(giocatore)
    }

    inner class GiocatoreViewHolder(private val binding: ItemGiocatoreModificaBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var idGiocatore: String
        fun bind(giocatore: Giocatore) {
            idGiocatore = giocatore.id
            binding.nomeTextView.text = giocatore.nome
            binding.cognomeTextView.text = giocatore.cognome

            Glide.with(binding.root)
                .load(giocatore.foto)
                .placeholder(R.drawable.placeholder_logo)
                .into(binding.fotoImageView)

            binding.btnEdit.setOnClickListener {
                val ctx = it.context
                if (ctx is FragmentActivity){
                    val fragmentTransaction = ctx.supportFragmentManager.beginTransaction()
                    val aggiungiGiocatoriFragment : Fragment = AggiungiModificaGiocatoriFragment()
                    val args = Bundle()
                    args.putString("idGiocatore", idGiocatore)
                    aggiungiGiocatoriFragment.arguments = args
                    fragmentTransaction.replace(R.id.fragmentContainer, aggiungiGiocatoriFragment)
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit()
                }
            }

            binding.btnDelete.setOnClickListener {
                DeleteConfirmationDialog.showConfirmationDialog(
                    it.context,
                    "Conferma eliminazione",
                    "Sei sicuro di voler eliminare questo elemento?",
                    object : OnConfirmListener {
                        override fun onConfirm() {
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
    class DiffCallback : DiffUtil.ItemCallback<Giocatore>() {
        override fun areItemsTheSame(oldItem: Giocatore, newItem: Giocatore): Boolean {
            return oldItem.id == newItem.id // Supponendo che ci sia un campo "id" o qualcosa di simile che identifica univocamente il Giocatore
        }

        override fun areContentsTheSame(oldItem: Giocatore, newItem: Giocatore): Boolean {
            return oldItem == newItem // Questo controlla che tutti i campi siano identici, inclusi punteggio e nome
        }
    }

}
