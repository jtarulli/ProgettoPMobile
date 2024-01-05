// GiornateActivity.kt
package com.example.progettopm.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giornata
import com.example.progettopm.view.GiornateAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GiornateActivity : AppCompatActivity() {
/*
    private lateinit var giornateAdapter: GiornateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giornata)

        val recyclerView: RecyclerView = findViewById(R.id.giornateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        giornateAdapter = GiornateAdapter()
        recyclerView.adapter = giornateAdapter

        loadGiornateData()

        val creaGiornataButton: Button = findViewById(R.id.creaGiornataButton)
        creaGiornataButton.setOnClickListener {
            Log.d("GiornateActivity", "Creazione Giornata Button Clicked")
            startActivity(CreaGiornataActivity.newIntent(this))
        }
    }

    private fun loadGiornateData() {
        val legaReference = FirebaseFirestore.getInstance().collection("leghe")
            .document(SessionManager.legaCorrenteId!!)
        val giornateCollection = FirebaseFirestore.getInstance().collection("giornate")

        giornateCollection.addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.w("Error in giornateCollection", "Si è verificato un errore", e)
                return@addSnapshotListener
            }
            if (querySnapshot != null) {
                updateGiornate(querySnapshot)
            }
        }

        giornateCollection.whereEqualTo("legaId", legaReference)
            .get()
            .addOnSuccessListener { updateGiornate(it) }
            .addOnFailureListener { exception ->
                Log.e("GiornateActivity", "Error loading giornate data: ${exception.message}")
            }
    }

    private fun updateGiornate(documents: QuerySnapshot) {
        val giornateList = mutableListOf<Giornata>()
        for (document in documents) {
            val giornata = document.toObject(Giornata::class.java)
            giornateList.add(giornata)
        }
        Log.d("Debug_list", "" + giornateList)
        giornateAdapter.submitList(giornateList)

   }

 */
}
