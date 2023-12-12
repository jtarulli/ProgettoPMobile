// MainActivity.kt
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.progettopm.HomeActivity
import com.example.progettopm.R  // Sostituisci "tuo_package" con il nome del tuo package
import com.example.progettopm.view.ClassificaActivity
import com.example.progettopm.view.GiocatoriActivity
import com.example.progettopm.view.StoricoActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var currentFragment: Fragment

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    switchFragment(HomeActivity())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_storico -> {
                    switchFragment(StoricoActivity())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_storico -> {
                    switchFragment(GiocatoriActivity())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_storico -> {
                    switchFragment(ClassificaActivity())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
        currentFragment = fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inizializza il fragment iniziale (può essere HomeActivity)
        currentFragment = HomeActivity()
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, currentFragment).commit()

        // Inizializza BottomNavigationView
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
