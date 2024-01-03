package com.example.progettopm.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.example.progettopm.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 1000 // 3 secondi

    // Definisci il BroadcastReceiver
    private val myReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Gestisci l'evento ricevuto qui
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Registra il BroadcastReceiver
        val filter = IntentFilter("com.example.progettopm.MY_ACTION")
        registerReceiver(myReceiver, filter)

        // Impostare un ritardo per lo splash screen e passare alla tua attività di login
        Handler(Looper.getMainLooper()).postDelayed({
            // Avvia la tua attività principale
            startActivity(Intent(this, LoginActivity::class.java))

            // Chiudi questa attività
            finish()
        }, SPLASH_TIME_OUT)
    }

    override fun onDestroy() {
        // Deregistra il BroadcastReceiver quando l'attività viene distrutta
        unregisterReceiver(myReceiver)
        super.onDestroy()
    }
}