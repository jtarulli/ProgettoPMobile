package com.example.progettopm.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.progettopm.R

class ClassificaFragment : Fragment(R.layout.fragment_classifica) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewClassifica: TextView = view.findViewById(R.id.textViewClassifica)
        textViewClassifica.text = "Classifica"

        Log.d("ClassificaFragment", "onViewCreated called")
    }
}
