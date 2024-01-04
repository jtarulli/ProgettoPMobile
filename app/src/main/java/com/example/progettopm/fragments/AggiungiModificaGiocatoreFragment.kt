package com.example.progettopm.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.progettopm.R
import com.example.progettopm.SessionManager
import com.example.progettopm.model.Giocatore
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class AggiungiModificaGiocatoriFragment : Fragment() {

    private lateinit var nomeTextField : EditText
    private lateinit var cognomeTextField : EditText
    private lateinit var budgetEditText: EditText
    private lateinit var imageView : ImageView
    private lateinit var confirmBtn : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_aggiunta_giocatore, container, false)
        val args = this.arguments
        nomeTextField = view.findViewById(R.id.nomeEditText)
        cognomeTextField = view.findViewById(R.id.surnameEditText)
        budgetEditText = view.findViewById(R.id.budgetEditText)
        imageView = view.findViewById(R.id.playerImageView)

        val idArg : String = args?.getString("idGiocatore") ?: ""
        if (idArg != ""){
            getGiocatoreDetails(idArg)
        }

        confirmBtn = view.findViewById(R.id.confermaButton)

        confirmBtn.setOnClickListener {
            addPLayer(idArg, hashMapOf(
                "nome" to nomeTextField.text.toString(),
                "cognome" to cognomeTextField.text.toString(),
                "valore" to budgetEditText.text.toString().toInt(),
                "foto" to "",
                "punteggio" to 0,
                "lega" to FirebaseFirestore.getInstance().collection("leghe")
                    .document(SessionManager.legaCorrenteId!!),
                "bonusGiornataList" to emptyList<DocumentReference>(),
                "id" to idArg
            ))
        }

        return view
    }

    private fun getGiocatoreDetails(id: String){
        FirebaseFirestore.getInstance().collection("giocatori")
            .document(id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var res = it.result
                    val giocatore : Giocatore? = res.toObject(Giocatore::class.java)
                    nomeTextField.setText(giocatore?.nome)
                    cognomeTextField.setText(giocatore?.cognome)

                    val value = giocatore?.valore
                    budgetEditText.setText("$value")

                    val foto = giocatore?.foto
                    //imageView.set
                }
            }.addOnFailureListener {
                Log.d("Excs", "Non c'è il calciatore $it")
            }
    }

    private fun addPLayer(
        idPlayer: String,
        playerData: HashMap<String, Any>
    ) {
        val giocatoriCollection = FirebaseFirestore.getInstance().collection("giocatori")

        // Controlla se stai aggiungendo un nuovo giocatore o modificando un giocatore esistente
        if (idPlayer.isNotEmpty()) {
            // Se stai modificando, aggiorna il documento esistente
            val player = giocatoriCollection.document(idPlayer)
            player.set(playerData)
                .addOnSuccessListener {
                    Toast.makeText(
                        activity,
                        "Successo",
                        Toast.LENGTH_SHORT
                    ).show()
                    parentFragmentManager.popBackStack()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        activity,
                        "Errore",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            // Se stai aggiungendo, crea un nuovo documento con un ID generato automaticamente
            giocatoriCollection.add(playerData)
                .addOnSuccessListener { documentReference ->
                    // Ottieni l'ID generato automaticamente e aggiornalo nel giocatore
                    val newPlayerId = documentReference.id
                    playerData["id"] = newPlayerId

                    // Aggiorna il documento appena creato con l'ID generato
                    giocatoriCollection.document(newPlayerId).set(playerData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                activity,
                                "Successo",
                                Toast.LENGTH_SHORT
                            ).show()
                            parentFragmentManager.popBackStack()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                activity,
                                "Errore: $e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        activity,
                        "Errore: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

}