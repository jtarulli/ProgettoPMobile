package com.example.progettopm.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Bonus
import com.example.progettopm.view.BonusAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class BonusActivity : AppCompatActivity() {

    private lateinit var bonusAdapter: BonusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bonus)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bonusAdapter = BonusAdapter()
        recyclerView.adapter = bonusAdapter

        loadBonusData()

        val creaBonusButton: Button = findViewById(R.id.creaBonusButton)
        creaBonusButton.setOnClickListener {
            // Avvia l'activity per la creazione di un nuovo bonus
            Log.d("BonusActivity", "Creazione Bonus Button Clicked")
            startActivity(CreaBonusActivity.newIntent(this))
        }
    }

    private fun loadBonusData() {
        // Recupera i bonus dalla raccolta 'bonus' con la reference alla lega corrente
        val legaReference = FirebaseFirestore.getInstance().collection("leghe")
                                                           .document(SessionManager.legaCorrenteId!!)
        val bonusCollection = FirebaseFirestore.getInstance().collection("bonus")
        bonusCollection.addSnapshotListener{ querySnapShot, e ->
            if(e != null){
                Log.w("Error in bonusCollection", "Si è verificato un errore", e)
                return@addSnapshotListener
            }
            if (querySnapShot != null) {
                updateBonus(querySnapShot)
            }
        }
        bonusCollection.whereEqualTo("lega", legaReference)
            .get()
            .addOnSuccessListener { updateBonus(it) }
            .addOnFailureListener { exception ->
                Log.e("BonusActivity", "Error loading bonus data: ${exception.message}")
                // Gestisci eventuali errori durante il recupero dei bonus
            }
    }

    private fun updateBonus(documents : QuerySnapshot){
        val bonusList = mutableListOf<Bonus>()
        for (document in documents) {
            val bonus = document.toObject(Bonus::class.java)
            bonusList.add(bonus)
        }
        Log.d("Debug_list", "" + bonusList)
        bonusAdapter.submitList(bonusList)
    }
}
