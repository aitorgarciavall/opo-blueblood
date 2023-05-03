package com.example.opoblueblood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TemaAdapter(private val temes: List<Tema>, private val onItemClick: (Tema) -> Unit) : RecyclerView.Adapter<TemaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tema_item, parent, false)
        return TemaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TemaViewHolder, position: Int) {
        val tema = temes[position]
        holder.tvTemaNom.text = tema.nom
        holder.itemView.setOnClickListener {
            onItemClick(tema)
        }
    }

    override fun getItemCount(): Int {
        return temes.size
    }
}
