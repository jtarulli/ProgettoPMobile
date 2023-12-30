package com.example.progettopm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettopm.R
import com.example.progettopm.firestore.MyAdapterStorico
import com.example.progettopm.model.Formazione
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject

class StoricoFragment : Fragment(R.layout.fragment_storico) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var formazioniArrayList: ArrayList<Formazione>
    private lateinit var myAdapter: MyAdapterStorico
    private lateinit var db: FirebaseFirestore
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.storicoRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        formazioniArrayList = arrayListOf()
        myAdapter = MyAdapterStorico(formazioniArrayList)
        recyclerView.adapter = myAdapter

        EventChangeListener()
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Formazioni").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return
                }

                for (dc : DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        formazioniArrayList.add(dc.document.toObject(Formazione::class.java))
                    }
                }

                myAdapter.notifyDataSetChanged()
            }

        })
    }

}