import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giornata
import com.example.progettopm.view.GiornateAdapter
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class CreaGiornataActivity : AppCompatActivity() {

    private lateinit var legaId: String
    private lateinit var dataInizioTextView: TextView
    private lateinit var oraInizioTextView: TextView
    private lateinit var dataFineTextView: TextView
    private lateinit var oraFineTextView: TextView
    private lateinit var confermaButton: Button
    private lateinit var giornateAdapter: GiornateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crea_giornata)

        legaId = SessionManager.legaCorrenteId.toString()

        dataInizioTextView = findViewById(R.id.textViewInizio)
        oraInizioTextView = findViewById(R.id.buttonInizio)
        dataFineTextView = findViewById(R.id.textViewFine)
        oraFineTextView = findViewById(R.id.buttonFine)
        confermaButton = findViewById(R.id.confermaButton)

        // Imposta i click listener per la scelta della data e dell'ora
        dataInizioTextView.setOnClickListener {
            mostraSceltaDataOra(true)
        }

        dataFineTextView.setOnClickListener {
            mostraSceltaDataOra(false)
        }

        confermaButton.setOnClickListener {
            confermaCreazioneGiornata()
        }

        // Inizializza l'adapter passando se stessa come parametro
        giornateAdapter = GiornateAdapter(mutableListOf(), this, this)
    }

    private fun mostraSceltaDataOra(isInizio: Boolean) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                mostraSceltaOra(isInizio, calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun mostraSceltaOra(isInizio: Boolean, calendar: Calendar) {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val formattedDate = dateFormat.format(calendar.time)
                val formattedTime = timeFormat.format(calendar.time)

                if (isInizio) {
                    dataInizioTextView.text = formattedDate
                    oraInizioTextView.text = formattedTime
                } else {
                    dataFineTextView.text = formattedDate
                    oraFineTextView.text = formattedTime
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

        timePickerDialog.show()
    }

    private fun confermaCreazioneGiornata() {
        val inizioMillis = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .parse("${dataInizioTextView.text} ${oraInizioTextView.text}").time

        val fineMillis = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .parse("${dataFineTextView.text} ${oraFineTextView.text}").time

        if (fineMillis > inizioMillis) {
            val nuovaGiornata = Giornata(
                getProssimoNumeroGiornata(),
                Instant.ofEpochMilli(inizioMillis),
                Instant.ofEpochMilli(fineMillis),
                legaId
            )

            // Salva la nuova giornata su Firebase Firestore
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("giornate")
                .add(
                    hashMapOf(
                        "id" to nuovaGiornata.id,
                        "inizio" to nuovaGiornata.inizio.toEpochMilli(),
                        "fine" to nuovaGiornata.fine.toEpochMilli(),
                        "lega" to nuovaGiornata.legaId
                    )
                )
                .addOnSuccessListener { documentReference ->
                    // Assegna l'ID convertito
                    nuovaGiornata.id = documentReference.id.toIntOrNull() ?: 0

                    Toast.makeText(
                        this,
                        "Giornata salvata con successo",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Chiudi l'activity dopo aver salvato la giornata
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Errore durante il salvataggio della giornata: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            // Seleziona un'ora di fine successiva a quella di inizio
            Toast.makeText(
                this,
                "La data e l'ora di fine devono essere successive a quelle di inizio",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun getProssimoNumeroGiornata(): Int {
        val giornateList = giornateAdapter.getGiornateList()

        // Ordina la lista per data di inizio in modo crescente
        val giornateOrdinate = giornateList.sortedBy { it.inizio }

        // Controlla se ci sono già giornate presenti
        if (giornateOrdinate.isNotEmpty()) {
            // Ottieni l'ultima giornata nella lista ordinata
            val ultimaGiornata = giornateOrdinate.last()

            // Il prossimo numero sarà l'ID dell'ultima giornata incrementato di uno
            return ultimaGiornata.id + 1
        } else {
            // Se non ci sono giornate presenti, il prossimo numero sarà 1
            return 1
        }
    }




}
