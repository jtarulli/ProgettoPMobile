package com.example.progettopm.view// com.example.progettopm.view.GiocatoriActivity.kt
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.adapters.GiocatoriAdapter
import com.example.progettopm.model.Giocatore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GiocatoriActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var giocatoriAdapter: GiocatoriAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giocatori)

        recyclerView = findViewById(R.id.recyclerViewGiocatori)
        giocatoriAdapter = GiocatoriAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = giocatoriAdapter

        loadGiocatoriFromFirestore()
    }

    private fun loadGiocatoriFromFirestore() {
        // Recupera i dati dei giocatori dal Firestore e aggiorna l'adapter
        firestore.collection("giocatori")
            .orderBy("punteggio", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val giocatoriList = ArrayList<Giocatore>()

                for (document in result) {
                    val giocatore = document.toObject(Giocatore::class.java)
                    giocatoriList.add(giocatore)
                }

                giocatoriAdapter.setData(giocatoriList)
            }
            .addOnFailureListener { exception ->
                // Gestisci eventuali errori durante il recupero dei dati
            }
    }
}

