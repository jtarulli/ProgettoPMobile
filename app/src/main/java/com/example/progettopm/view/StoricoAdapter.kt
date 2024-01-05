package com.example.progettopm.view

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Formazione
import com.example.progettopm.model.Giocatore
import com.example.progettopm.model.BonusGiornata
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class StoricoAdapter(private val formazioni: List<Formazione>) : RecyclerView.Adapter<StoricoAdapter.StoricoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoricoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_storico, parent, false)
        return StoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoricoViewHolder, position: Int) {
        val formazione = formazioni[position]
        holder.bind(formazione)
    }

    override fun getItemCount(): Int {
        return formazioni.size
    }

    class StoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(formazione: Formazione) {
            // Trova le TextView
            val numGiornataTextView = itemView.findViewById<TextView>(R.id.numGiornata_RV)
            val nomeSquadraTextView = itemView.findViewById<TextView>(R.id.nomeSquadra_RV)
            nomeSquadraTextView.text = formazione.squadra?.toString() ?: "Nome Squadra non disponibile"
            numGiornataTextView.text = formazione.giornata.toString()

            // RecyclerView per gli elenchi di giocatori
            val elencoGiocatoriRecyclerView = itemView.findViewById<RecyclerView>(R.id.elencoGiocatori_Rv)

            // Inizializza e associa l'adapter alla RecyclerView interna
            val giocatoriAdapter = GiocatoriAdapter(formazione.giocatori)
            elencoGiocatoriRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            elencoGiocatoriRecyclerView.adapter = giocatoriAdapter
        }
    }

    class GiocatoriAdapter(private val giocatori: List<DocumentReference>) : RecyclerView.Adapter<GiocatoriAdapter.GiocatoreViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiocatoreViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_giocatore_storico, parent, false)
            return GiocatoreViewHolder(view)
        }

        override fun onBindViewHolder(holder: GiocatoreViewHolder, position: Int) {
            val giocatoreRef = giocatori[position]
            holder.bind(giocatoreRef)
        }

        override fun getItemCount(): Int {
            return giocatori.size
        }

        class GiocatoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val bonusAdapter = BonusGiornataAdapter()

            init {
                // Inizializza il RecyclerView per gli elenchi di bonus
                itemView.findViewById<RecyclerView>(R.id.recyclerViewBonusGiornata).apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = bonusAdapter
                }
            }

            fun bind(giocatoreRef: DocumentReference) {
                giocatoreRef.get().addOnSuccessListener { giocatoreSnapshot ->
                    val giocatore = giocatoreSnapshot.toObject(Giocatore::class.java)

                    // Visualizza i dettagli del giocatore
                    Log.d("GiocatoreViewHolder", "Nome: ${giocatore?.nome}, Cognome: ${giocatore?.cognome}, Punteggio: ${giocatore?.punteggio}, Valore: ${giocatore?.valore}")

                    itemView.findViewById<TextView>(R.id.nomeGiocatore_RV)?.text = "${giocatore?.nome} ${giocatore?.cognome}"
                    itemView.findViewById<TextView>(R.id.punteggioGiocatore_RV)?.text = giocatore?.punteggio.toString()
                    itemView.findViewById<TextView>(R.id.valoreGiocatore_RV)?.text = giocatore?.valore.toString()

                    // Recupera la lista di BonusGiornata in modo asincrono
                    GlobalScope.launch(Dispatchers.Main) {
                        val bonusGiornataList = retrieveBonusGiornataList(giocatore?.bonusGiornataList)
                        bonusAdapter.submitList(bonusGiornataList)
                    }
                }
            }

            private suspend fun retrieveBonusGiornataList(bonusGiornataRefList: List<DocumentReference>?): List<BonusGiornata> {
                return withContext(Dispatchers.IO) {
                    val bonusGiornataList = mutableListOf<BonusGiornata>()

                    bonusGiornataRefList?.forEach { bonusGiornataRef ->
                        try {
                            val bonusSnapshot = bonusGiornataRef.get().await()
                            val bonusGiornata = bonusSnapshot.toObject(BonusGiornata::class.java)
                            if (bonusGiornata != null) {
                                bonusGiornataList.add(bonusGiornata)
                            }
                        } catch (e: Exception) {
                            Log.e("GiocatoreViewHolder", "Error retrieving BonusGiornata: ${e.message}")
                        }
                    }

                    bonusGiornataList
                }
            }
        }

        // Adapter integrato BonusGiornata
        class BonusGiornataAdapter : RecyclerView.Adapter<BonusGiornataAdapter.BonusGiornataViewHolder>() {

            private var bonusGiornataList: List<BonusGiornata> = listOf()

            fun submitList(list: List<BonusGiornata>) {
                bonusGiornataList = list
                notifyDataSetChanged()
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusGiornataViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bonus_giornata, parent, false)
                return BonusGiornataViewHolder(view)
            }

            override fun onBindViewHolder(holder: BonusGiornataViewHolder, position: Int) {
                val bonusGiornata = bonusGiornataList[position]
                holder.bind(bonusGiornata)
            }

            override fun getItemCount(): Int {
                return bonusGiornataList.size
            }

            class BonusGiornataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(bonusGiornata: BonusGiornata) {
                    Log.d("BonusGiornataViewHolder", "Quantità: ${bonusGiornata.quantita}, TipoBonus: ${bonusGiornata.tipoBonus}")

                    itemView.findViewById<TextView>(R.id.quantitaTextView)?.text = bonusGiornata.quantita.toString()
                    itemView.findViewById<TextView>(R.id.nomeTipoBonusTextView)?.text = bonusGiornata.tipoBonus.toString()
                }
            }
        }
    }
}
