import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.progettopm.R
import com.example.progettopm.model.Lega

class UnioneLegaAdapter(private val onUniscitiClick: (Lega) -> Unit) :
    ListAdapter<Lega, UnioneLegaAdapter.LegaViewHolder>(LegaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lega, parent, false)
        return LegaViewHolder(view)
    }

    override fun onBindViewHolder(holder: LegaViewHolder, position: Int) {
        val lega = getItem(position)
        holder.bind(lega, onUniscitiClick)
    }

    class LegaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.nomeLegaTextView)
        private val budgetTextView: TextView = itemView.findViewById(R.id.budgetLegaTextView)
        private val logoImageView: ImageView = itemView.findViewById(R.id.logoLegaImageView)
        private val uniscitiButton: Button = itemView.findViewById(R.id.uniscitiButton)

        fun bind(lega: Lega, onUniscitiClick: (Lega) -> Unit) {
            nomeTextView.text = lega.nome
            budgetTextView.text = lega.budget.toString()

            // Carica l'immagine del logo con Glide (assicurati di aggiungere la dipendenza di Glide nel tuo build.gradle)
            Glide.with(itemView.context)
                .load(lega.logo)
                .placeholder(R.drawable.placeholder_logo) // Sostituisci con il tuo placeholder
                .into(logoImageView)

            // Gestisci il clic sul pulsante "Unisciti"
            uniscitiButton.setOnClickListener {
                onUniscitiClick(lega)
            }
        }
    }

    private class LegaDiffCallback : DiffUtil.ItemCallback<Lega>() {
        override fun areItemsTheSame(oldItem: Lega, newItem: Lega): Boolean {
            return oldItem.nome == newItem.nome
        }

        override fun areContentsTheSame(oldItem: Lega, newItem: Lega): Boolean {
            return oldItem == newItem
        }
    }
}
