package com.example.progettopm.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.progettopm.R
import com.example.progettopm.databinding.ActivityMasterBinding
import com.example.progettopm.fragments.ClassificaFragment
import com.example.progettopm.fragments.HomeFragment
import com.example.progettopm.fragments.StoricoFragment
import com.example.progettopm.ui.GiocatoriFragment
import com.example.progettopm.ui.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MasterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        //Navigation View
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout_master)
        val navView: NavigationView = findViewById(R.id.navView_master)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Nav header
        // Recupera l'utente corrente da Firebase Authentication
        // Aggiungi il nome dell'utente al Navigation Header
        val headerView = navView.getHeaderView(0)
        val textViewHeader: TextView = headerView.findViewById(R.id.user_navHeader_TV)

        // Recupera l'utente corrente da Firebase Authentication
        val user = FirebaseAuth.getInstance().currentUser
        // Verifica se l'utente è loggato
        if (user != null) {
            // L'utente è loggato, puoi accedere alle informazioni sull'utente, ad esempio l'email
            val email = user.email
            if (email != null) {
                // Ora puoi utilizzare l'email dell'utente come preferisci, ad esempio visualizzandola in un TextView
                textViewHeader.text = email
            }
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout_item -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        //Navigation Bar
        binding.navBarMaster.setOnItemSelectedListener { menuItem ->
            Log.d("MasterActivity", "setOnItemSelectedListener called")
            when (menuItem.itemId) {
                R.id.home_nav_bar -> {
                    Log.d("MasterActivity", "Home selected")
                    replaceFragment(HomeFragment())
                }
                R.id.storico_nav_bar -> {
                    Log.d("MasterActivity", "Storico selected")
                    replaceFragment(StoricoFragment())
                }
                R.id.classifica_nav_bar -> {
                    Log.d("MasterActivity", "Classifica selected")
                    replaceFragment(ClassificaFragment())
                }
                R.id.giocatori_nav_bar -> {
                    Log.d("MasterActivity", "Giocatori selected")
                    replaceFragment(GiocatoriFragment())
                }
            }
            true
        }


    }
    //Funzioni
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
        Log.d("MasterActivity", "replaceFragment called")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_bar, menu)
        return true
    }

    private fun changeActivity(nomeActivity: Class<*>) {
        val intent = Intent(this, nomeActivity)
        startActivity(intent)
        finish()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        // Dopo aver eseguito il logout, riporta l'utente alla schermata di accesso
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}