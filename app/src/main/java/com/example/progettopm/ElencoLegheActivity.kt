import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ElencoLegheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elenco_leghe)

        val legheListView: ListView = findViewById(R.id.legheListView)

        // Inizializza il database Firebase
        val db = Firebase.firestore

        // Recupera l'elenco delle leghe dalla raccolta "Leghe" nel database
        db.collection("Leghe")
            .get()
            .addOnSuccessListener { result ->
                val legheList: MutableList<String> = mutableListOf()
                for (document in result) {
                    val lega = document.data["nome"] as String
                    legheList.add(lega)
                }
                // Crea un adapter per la ListView e imposta l'elenco delle leghe
                val adapter = LegheAdapter(this, legheList)
                legheListView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Gestisci l'errore durante il recupero delle leghe
            }
    }
}

class LegheAdapter(context: Context, leghe: List<String>) : ArrayAdapter<String>(context, 0, leghe) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.lega_item, parent, false)
        }

        val lega = getItem(position)
        val nomeLegaTextView: TextView = view!!.findViewById(R.id.nomeLegaTextView)
        nomeLegaTextView.text = lega
        return view
    }
}