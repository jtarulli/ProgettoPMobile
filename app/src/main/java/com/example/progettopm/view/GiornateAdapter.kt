// GiornateAdapter.kt
package com.example.progettopm.view

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.DeleteConfirmationDialog
import com.example.progettopm.R
import com.example.progettopm.databinding.ItemGiornataBinding
import com.example.progettopm.DeleteConfirmationDialog.OnConfirmListener
import com.example.progettopm.model.Giornata
import com.google.firebase.firestore.FirebaseFirestore

class GiornateAdapter() :
    /*

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiornataViewHolder {
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
            binding.inizioTextView.text = giornata.inizio.toString()
            binding.fineTextView.text = giornata.fine.toString()

            binding.btnEdit.setOnClickListener {
                val ctx = it.context
                if (ctx is FragmentActivity){
                    val fragmentTransaction = ctx.supportFragmentManager.beginTransaction()
                    val aggiungiGiornateFragment : Fragment = AggiungiModificaGiornateFragment()
                    val args = Bundle()
                    args.putString("idGiornata", idGiornata)
                    aggiungiGiornateFragment.arguments = args
                    fragmentTransaction.replace(R.id.fragmentContainer, aggiungiGiornateFragment)
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit()
                }
            }

            binding.btnDelete.setOnClickListener {
                DeleteConfirmationDialog.showConfirmationDialog(
                    it.context,
                    "Conferma eliminazione",
                    "Sei sicuro di voler eliminare questa giornata?",
                    object : OnConfirmListener {
                        override fun onConfirm() {
                            val giornataId = getItem(adapterPosition).id
                            itemClickListener?.onDelete(giornataId)
                        }
                    })
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

     */
    Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GiornateAdapter> {
        override fun createFromParcel(parcel: Parcel): GiornateAdapter {
            return GiornateAdapter(parcel)
        }

        override fun newArray(size: Int): Array<GiornateAdapter?> {
            return arrayOfNulls(size)
        }
    }
}