package com.example.tcc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.R
import com.example.tcc.model.Banda

class BandaAdapter(private var listaBandas: ArrayList<Banda>) : RecyclerView.Adapter<BandaAdapter.MyViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textoNomeBanda: TextView
        var textoGenero: TextView

        init {
            textoNomeBanda = view.findViewById(R.id.textoNomeBanda)
            textoGenero = view.findViewById(R.id.textoGenero)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BandaAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banda, parent, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textoNomeBanda.text = listaBandas.get(position).nome
        holder.textoGenero.text = listaBandas.get(position).genero
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount() = listaBandas.size

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}