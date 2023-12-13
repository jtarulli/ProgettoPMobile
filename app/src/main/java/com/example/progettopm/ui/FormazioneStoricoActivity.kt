import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.model.Bonus
import com.example.progettopm.model.BonusGiornata
import com.example.progettopm.model.Giocatore
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Arrays

class FormazioniStoricoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var formazioniAdapter: FormazioniAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formazioni_storico)

        recyclerView = findViewById(R.id.recyclerViewFormazioni)
        formazioniAdapter = FormazioniAdapter(getFormazioniOptions())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = formazioniAdapter
    }

    private fun getFormazioniOptions(): FirestoreRecyclerOptions<Formazione> {
        val query = firestore.collection("formazioni")
        val options = FirestoreRecyclerOptions.Builder<Formazione>()
            .setQuery(query, Formazione::class.java)
            .build()
        return options
    }

    override fun onStart() {
        super.onStart()
        formazioniAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        formazioniAdapter.stopListening()
    }

    inner class FormazioniAdapter(options: FirestoreRecyclerOptions<Formazione>) :
        FirestoreRecyclerAdapter<Formazione, FormazioniAdapter.FormazioneViewHolder>(options) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FormazioneViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_formazione_storico, parent, false)
            return FormazioneViewHolder(view)
        }
        override fun onBindViewHolder(
            holder: FormazioneViewHolder,
            position: Int,
            model: Formazione
        ) {
            holder.bind(model)
        }
        inner class FormazioneViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            private val numGiornataTextView: TextView =
                itemView.findViewById(R.id.numGiornataTextView)
            private val punteggioTextView: TextView =
                itemView.findViewById(R.id.punteggioTextView)
            private val espandiButton: Button = itemView.findViewById(R.id.espandiButton)

            private lateinit var giocatoriAdapter: GiocatoriAdapter
            private lateinit var giocatoriRecyclerView: RecyclerView

            init {
                espandiButton.setOnClickListener {
                    // Gestisci l'espansione della formazione
                    // Mostra i giocatori nel modo desiderato
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val formazione = getItem(position)
                        // Passa la formazione al metodo per gestire l'espansione
                        handleEspansione(formazione)
                    }
                }
            }

            fun bind(formazione: Formazione) {
                numGiornataTextView.text = formazione.giornata.toString()
                punteggioTextView.text = formazione.punteggio.toString()

                // Inizializza e imposta l'adapter per i giocatori
                giocatoriAdapter = GiocatoriAdapter(getGiocatoriOptions(formazione))
                giocatoriRecyclerView = itemView.findViewById(R.id.giocatoriRecyclerView)
                giocatoriRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
                giocatoriRecyclerView.adapter = giocatoriAdapter

                // Chiamare startListening per iniziare a ricevere gli aggiornamenti in tempo reale
                giocatoriAdapter.startListening()
            }

            private fun getGiocatoriOptions(formazione: Formazione): FirestoreRecyclerOptions<Giocatore> {
                // Costruisci la query per ottenere i giocatori della formazione corrente
                val query = firestore.collection("giocatori")
                    .whereIn(
                        "id", Arrays.asList(
                            formazione.portiere.id,
                            formazione.difensore.id,
                            formazione.laterale_dx.id,
                            formazione.laterale_sx.id,
                            formazione.attaccante.id
                        ))

                // Costruisci le opzioni per il FirestoreRecyclerAdapter
                val options = FirestoreRecyclerOptions.Builder<Giocatore>()
                    .setQuery(query, Giocatore::class.java)
                    .build()

                return options
            }

            private fun handleEspansione(formazione: Formazione) {
                // Qui gestisci l'espansione della formazione
                // Ad esempio, puoi mostrare/nascondere la RecyclerView dei giocatori
                // o effettuare altre operazioni a tuo piacimento
            }
        }
    }

    inner class GiocatoriAdapter(options: FirestoreRecyclerOptions<Giocatore>) :
        FirestoreRecyclerAdapter<Giocatore, GiocatoriAdapter.GiocatoreViewHolder>(options) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): GiocatoreViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_giocatore, parent, false)
            return GiocatoreViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: GiocatoreViewHolder,
            position: Int,
            model: Giocatore
        ) {
            holder.bind(model)
        }


        inner class GiocatoreViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            private val nomeTextView: TextView = itemView.findViewById(R.id.nomeTextView)
            private val cognomeTextView: TextView = itemView.findViewById(R.id.cognomeTextView)
            private val squadraTextView: TextView = itemView.findViewById(R.id.squadraTextView)
            private val fotoTextView: TextView = itemView.findViewById(R.id.fotoTextView)
            private val punteggioTextView: TextView = itemView.findViewById(R.id.punteggioTextView)
            private val espandiButton: Button = itemView.findViewById(R.id.espandiButton)

            private lateinit var bonusAdapter: BonusAdapter
            private lateinit var bonusRecyclerView: RecyclerView

            init {
                espandiButton.setOnClickListener {
                    // Gestisci l'espansione del giocatore
                    // Mostra i bonus nel modo desiderato
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val giocatore = getItem(position)
                        // Passa il giocatore al metodo per gestire l'espansione
                        handleEspansioneGiocatore(giocatore)
                    }
                }
            }

            fun bind(giocatore: Giocatore) {
                nomeTextView.text = giocatore.nome
                cognomeTextView.text = giocatore.cognome
                squadraTextView.text = giocatore.squadra
                fotoTextView.text = giocatore.foto
                punteggioTextView.text = giocatore.punteggio.toString()

                // Inizializza e imposta l'adapter per i bonus
                bonusAdapter = BonusAdapter(getBonusOptions(giocatore))
                bonusRecyclerView = itemView.findViewById(R.id.bonusRecyclerView)
                bonusRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
                bonusRecyclerView.adapter = bonusAdapter

                // Chiamare startListening per iniziare a ricevere gli aggiornamenti in tempo reale
                bonusAdapter.startListening()
            }

            private fun getBonusOptions(giocatore: Giocatore): FirestoreRecyclerOptions<BonusGiornata> {
                // Costruisci la query per ottenere i bonus giornata del giocatore corrente
                val query = firestore.collection("bonusgiornata")
                    .whereEqualTo("giocatore", giocatore.id)

                // Costruisci le opzioni per il FirestoreRecyclerAdapter
                val options = FirestoreRecyclerOptions.Builder<BonusGiornata>()
                    .setQuery(query, BonusGiornata::class.java)
                    .build()

                return options
            }

            private fun handleEspansioneGiocatore(giocatore: Giocatore) {
                // Qui gestisci l'espansione del giocatore
                // Ad esempio, puoi mostrare/nascondere la RecyclerView dei bonus
                // o effettuare altre operazioni a tuo piacimento
            }
        }
    }

    // ...

    inner class BonusAdapter(options: FirestoreRecyclerOptions<BonusGiornata>) :
        FirestoreRecyclerAdapter<BonusGiornata, BonusAdapter.BonusViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bonus, parent, false)
            return BonusViewHolder(view)
        }

        override fun onBindViewHolder(holder: BonusViewHolder, position: Int, model: BonusGiornata) {
            holder.bind(model)
        }
        inner class BonusViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            private val nomeTipoBonusTextView: TextView = itemView.findViewById(R.id.nomeTipoBonusTextView)
            private val valoreTipoBonusTextView: TextView = itemView.findViewById(R.id.valoreTipoBonusTextView)
            private val quantitaTextView: TextView = itemView.findViewById(R.id.quantitaTextView)

            fun bind(bonusGiornata: BonusGiornata) {
                // Ottieni la referenza al documento Bonus
                val tipoBonusReference = bonusGiornata.tipoBonus
                // Ottieni il documento Bonus associato alla referenza
                tipoBonusReference.get().addOnSuccessListener { tipoBonusSnapshot ->
                    if (tipoBonusSnapshot.exists()) {
                        // Il documento Bonus esiste
                        val tipoBonus = tipoBonusSnapshot.toObject(Bonus::class.java)
                        // Aggiorna gli elementi del layout con i dati del Bonus
                        nomeTipoBonusTextView.text = tipoBonus?.nome
                        valoreTipoBonusTextView.text = tipoBonus?.valore.toString()
                        quantitaTextView.text = bonusGiornata.quantita.toString()
                    } else {
                        // Il documento Bonus non esiste
                        // Gestisci il caso in cui il documento Bonus non sia presente
                    }
                }.addOnFailureListener { exception ->
                    // Gestisci eventuali errori nel recupero del documento Bonus
                    Log.e(TAG, "Errore nel recupero del documento Bonus", exception)
                }
            }
        }
    }
}
