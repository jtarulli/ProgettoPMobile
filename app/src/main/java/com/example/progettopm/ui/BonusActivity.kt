package com.example.progettopm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Bonus
import com.example.progettopm.view.BonusAdapter
import com.google.firebase.firestore.FirebaseFirestore

class BonusActivity : AppCompatActivity(), BonusAdapter.BonusAdapterListener {

    private lateinit var bonusAdapter: BonusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bonus)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bonusAdapter = BonusAdapter(this)
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
        val legaReference = SessionManager.legaCorrenteId
        val bonusCollection = FirebaseFirestore.getInstance().collection("bonus")
        bonusCollection.whereEqualTo("lega", legaReference)
            .get()
            .addOnSuccessListener { documents ->
                val bonusList = mutableListOf<Bonus>()
                for (document in documents) {
                    val bonus = document.toObject(Bonus::class.java)
                    bonusList.add(bonus)
                }
                bonusAdapter.setBonusList(bonusList)
            }
            .addOnFailureListener { exception ->
                Log.e("BonusActivity", "Error loading bonus data: ${exception.message}")
                // Gestisci eventuali errori durante il recupero dei bonus
            }
    }

    override fun onModificaButtonClick(bonus: Bonus) {
        // Avvia l'activity per la modifica del bonus passando l'oggetto Bonus
        Log.d("BonusActivity", "Modifica Button Clicked")
        startActivity(CreaBonusActivity.editIntent(this, bonus))
    }

    override fun onEliminaButtonClick(bonus: Bonus) {
        // Mostra un dialog di conferma eliminazione
        AlertDialog.Builder(this)
            .setTitle("Conferma eliminazione")
            .setMessage("Confermi l'eliminazione del bonus?")
            .setPositiveButton("Conferma") { _, _ ->
                // Se l'utente conferma, procedi con l'eliminazione
                Log.d("BonusActivity", "Elimina Button Clicked")
                eliminaBonus(bonus)
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun eliminaBonus(bonus: Bonus) {
        // Ottieni l'ID del documento Bonus
        val documentId = bonus.id

        // Verifica se l'ID è valido prima di procedere
        if (documentId.isNotEmpty()) {
            // Elimina il documento dal Firestore utilizzando l'ID
            FirebaseFirestore.getInstance().collection("bonus").document(documentId)
                .delete()
                .addOnSuccessListener {
                    // Se l'eliminazione ha successo, aggiorna la lista dei bonus
                    loadBonusData()
                    Toast.makeText(this, "Bonus eliminato con successo", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.e("BonusActivity", "Error deleting bonus: ${it.message}")
                    // In caso di errore nell'eliminazione, gestisci l'errore di conseguenza
                    Toast.makeText(this, "Errore durante l'eliminazione del bonus", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            // L'ID del documento non è valido, gestisci di conseguenza
            Toast.makeText(this, "ID del documento non valido", Toast.LENGTH_SHORT).show()
        }
    }
}
