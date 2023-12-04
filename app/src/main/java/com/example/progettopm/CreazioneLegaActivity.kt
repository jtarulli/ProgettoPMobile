import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R

class CreazioneLegaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_lega)

        val pubblicaButton: Button = findViewById(R.id.pubblicaButton)
        val privataButton: Button = findViewById(R.id.privataButton)

        pubblicaButton.setOnClickListener {
            val intent = Intent(this, CompilaDatiActivity::class.java)
            intent.putExtra("tipoLega", "pubblica")
            startActivity(intent)
        }

        privataButton.setOnClickListener {
            val intent = Intent(this, CompilaDatiActivity::class.java)
            intent.putExtra("tipoLega", "privata")
            startActivity(intent)
        }
    }
}

class CompilaDatiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compila_dati)

        val logoButton: Button = findViewById(R.id.logoButton)
        val nomeEditText: EditText = findViewById(R.id.nomeEditText)
        val budgetEditText: EditText = findViewById(R.id.budgetEditText)
        val parolaDOrdineEditText: EditText = findViewById(R.id.parolaDOrdineEditText)
        val prossimoButton: Button = findViewById(R.id.prossimoButton)

        logoButton.setOnClickListener {
            // Gestione del clic sul pulsante per selezionare il logo
            // ...
        }

        prossimoButton.setOnClickListener {
            val intent = Intent(this, ConfermaDatiActivity::class.java)
            intent.putExtra("nome", nomeEditText.text.toString())
            intent.putExtra("budget", budgetEditText.text.toString())
            intent.putExtra("parolaDOrdine", parolaDOrdineEditText.text.toString())
            startActivity(intent)
        }
    }
}

class ConfermaDatiActivity {

}
