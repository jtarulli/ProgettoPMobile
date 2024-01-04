package com.example.progettopm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Giornata
import java.text.SimpleDateFormat
import java.util.*

class GiornateAdapter(private val giornateList: List<Giornata>) : RecyclerView.Adapter<GiornateAdapter.GiornataViewHolder>() {

    class GiornataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inizioTextView: TextView = itemView.findViewById(R.id.inizioTextView)
        val fineTextView: TextView = itemView.findViewById(R.id.fineTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiornataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_giornata, parent, false)
        return GiornataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GiornataViewHolder, position: Int) {
        val giornata = giornateList[position]

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.inizioTextView.text = "Inizio: ${dateFormat.format(Date.from(giornata.inizio))}"
        holder.fineTextView.text = "Fine: ${dateFormat.format(Date.from(giornata.fine))}"
    }

    override fun getItemCount(): Int {
        return giornateList.size
    }
}
