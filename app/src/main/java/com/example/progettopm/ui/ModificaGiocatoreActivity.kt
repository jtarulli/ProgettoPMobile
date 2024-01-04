package com.example.progettopm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.progettopm.fragments.ModificaGiocatoriDefaultFragment


class ModificaGiocatoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.progettopm.R.layout.activity_modifica_giocatori)

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        val defaultFragment = ModificaGiocatoriDefaultFragment()

        fragmentTransaction.replace(com.example.progettopm.R.id.fragmentContainer, defaultFragment)
        fragmentTransaction.commit()
    }
}