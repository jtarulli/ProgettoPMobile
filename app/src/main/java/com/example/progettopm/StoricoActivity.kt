import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.progettopm.R

class StoricoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storico) // Assicurati di avere un layout con nome activity_storico.xml

        // Gestione del click sulla freccia per la 1° FORMAZIONE
        val arrow1 = findViewById<ImageView>(R.id.imageViewArrow1)
        val layoutCalciatori1 = findViewById<LinearLayout>(R.id.linearLayoutCalciatori1)
        arrow1.setOnClickListener {
            if (layoutCalciatori1.visibility == View.GONE) {
                layoutCalciatori1.visibility = View.VISIBLE
                arrow1.setImageResource(R.drawable.ic_arrow_up)
            } else {
                layoutCalciatori1.visibility = View.GONE
                arrow1.setImageResource(R.drawable.ic_arrow_down)
            }
        }

        // Altre gestioni delle frecce e dei layout per le altre formazioni...

    }
}