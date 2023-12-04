package com.example.progettopm

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ElencoLegheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elenco_leghe)

        val db = Firebase.firestore
        val legheList: MutableList<String> = mutableListOf()

        db.collection("Leghe")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val lega = document.data["nome"] as String
                    legheList.add(lega)
                }
                val legheListView: ListView = findViewById(R.id.legheListView)
                val adapter = LegheAdapter(this, legheList)
                legheListView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Errore durante il recupero delle leghe: $exception")
            }
    }
}
class LegheAdapter(context: Context, private val legheList: List<String>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return legheList.size
    }
    override fun getItem(position: Int): Any {
        return legheList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.lega_item, null)
            holder = ViewHolder()
            holder.nomeLegaTextView = view.findViewById(R.id.nomeLegaTextView)
            holder.uniscitiButton = view.findViewById(R.id.uniscitiButton)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val lega = legheList[position]
        holder.nomeLegaTextView.text = lega
        holder.uniscitiButton.setOnClickListener {
            // Gestisci il clic sul pulsante "UNISCITI"
            // ...
        }

        return view
    }

    private class ViewHolder {
        lateinit var nomeLegaTextView: TextView
        lateinit var uniscitiButton: Button
    }
}