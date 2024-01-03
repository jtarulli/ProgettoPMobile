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
import com.example.progettopm.SessionManager
import com.example.progettopm.databinding.ActivityMasterBinding
import com.example.progettopm.fragments.AdminLegaDashboardFragment
import com.example.progettopm.fragments.ClassificaFragment
import com.example.progettopm.fragments.HomeFragment
import com.example.progettopm.fragments.StoricoFragment
import com.example.progettopm.fragments.GiocatoriFragment
import com.example.progettopm.ui.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MasterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout_master)
        val navView: NavigationView = findViewById(R.id.navView_master)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val headerView = navView.getHeaderView(0)
        val textViewHeader: TextView = headerView.findViewById(R.id.user_navHeader_TV)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val email = user.email
            if (email != null) {
                textViewHeader.text = email
            }
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout_item -> {
                    logout()
                    true
                }
                R.id.dashboard_admin_lega -> {
                    isUserAdminLega(FirebaseAuth.getInstance().currentUser?.uid ?: "") { isAdmin ->
                        if (isAdmin) {
                            replaceFragment(AdminLegaDashboardFragment())
                        } else {
                            Toast.makeText(this, "Devi essere l'amministratore della lega per accedere a questa funzione", Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                else -> false
            }
        }

        binding.navBarMaster.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_nav_bar -> {
                    replaceFragment(HomeFragment())
                }
                R.id.storico_nav_bar -> {
                    replaceFragment(StoricoFragment())
                }
                R.id.classifica_nav_bar -> {
                    replaceFragment(ClassificaFragment())
                }
                R.id.giocatori_nav_bar -> {
                    replaceFragment(GiocatoriFragment())
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
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
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun isUserAdminLega(userId: String, callback: (Boolean) -> Unit) {
        val legaCorrenteId = SessionManager.legaCorrenteId

        if (legaCorrenteId != null) {
            val legaRef = FirebaseFirestore.getInstance().collection("leghe").document(legaCorrenteId)

            legaRef.get().addOnSuccessListener { legaDoc ->
                if (legaDoc.exists()) {
                    val adminId = legaDoc.getString("admin")
                    callback.invoke(adminId == userId)
                } else {
                    callback.invoke(false)
                }
            }
        } else {
            callback.invoke(false)
        }
    }
}