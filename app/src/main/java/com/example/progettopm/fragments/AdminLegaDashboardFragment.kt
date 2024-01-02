import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.progettopm.R
import com.example.progettopm.ui.ModificaLegaActivity

class AdminLegaDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_lega_dashboard, container, false)

        // Imposta il titolo della toolbar
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Admin Lega Dashboard"

        // Bottone "Modifica Lega"
        val modificaLegaButton = view.findViewById<Button>(R.id.modificaLegaButton)
        modificaLegaButton.setOnClickListener {
            // Avvia l'activity di CreazioneLegaActivity in modalità di modifica
            val intent = Intent(activity, ModificaLegaActivity::class.java)
            intent.putExtra("MODIFICA_LEGA", true)
            startActivity(intent)
        }

        return view
    }
}
