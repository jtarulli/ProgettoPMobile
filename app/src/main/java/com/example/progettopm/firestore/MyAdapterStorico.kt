package com.example.progettopm.firestore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Formazione
import com.google.firebase.firestore.FirebaseFirestore

class MyAdapterStoricoGiocatori(private val formazioniListInner: ArrayList<Formazione>) : RecyclerView.Adapter<MyAdapterStoricoGiocatori.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_storico_formazione, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val formazione: Formazione = formazioniListInner[position]
        holder.giocatore.text = formazione.giocatoriSchierati.toString()
        holder.punteggioGiocatore.text = formazione.giocatoriSchierati.toString()
    }

    override fun getItemCount(): Int {
        return formazioniListInner.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val giocatore: TextView = itemView.findViewById(R.id.giocatore_storico_RV)
        val punteggioGiocatore: TextView = itemView.findViewById(R.id.punteggioGiocatore_storico_Rv)
    }
}

class MyAdapterStorico(private val formazioniList: ArrayList<Formazione>, private val firestore: FirebaseFirestore) : RecyclerView.Adapter<MyAdapterStorico.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_storico, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val formazione: Formazione = formazioniList[position]
        holder.squadra.text = formazione.nomeSquadra
        holder.giornata.text = formazione.giornata.toString()

        // Recupera i dati del documento Firestore referenziato
        val documentRef = firestore.collection("il_tuo_percorso").document(formazione.giocatoriSchierati.id)
        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (document.exists()) {
                        val giocatoriSchierati = document.toObject(ArrayList::class.java)
                        holder.innerRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                        holder.innerRecyclerView.adapter = MyAdapterStoricoGiocatori(giocatoriSchierati)
                    } else {
                        Log.d("TAG", "Il documento non esiste")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Recupero del documento fallito", exception)
            }
    }

    override fun getItemCount(): Int {
        return formazioniList.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val squadra: TextView = itemView.findViewById(R.id.nomeSquadra_RV)
        val giornata: TextView = itemView.findViewById(R.id.numGiornata_RV)
        val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.elencoGiocatori_Rv)
    }
}
    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val squadra: TextView = itemView.findViewById(R.id.nomeSquadra_RV)
        val giornata: TextView = itemView.findViewById(R.id.numGiornata_RV)
        val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.elencoGiocatori_Rv)
    }
