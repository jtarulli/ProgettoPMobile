import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ElencoLegheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elenco_leghe)

        val legheListView: ListView = findViewById(R.id.legheListView)


    }
}
