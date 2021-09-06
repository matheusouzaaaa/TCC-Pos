package com.example.tcc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.R
import com.example.tcc.model.EstudioPlace

class EstudioPlaceAdapter(private var listaEstudios: ArrayList<EstudioPlace>) : RecyclerView.Adapter<EstudioPlaceAdapter.MyViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textoNome: TextView
        var textoEndereco: TextView
        var textoTelefone: TextView

        init {
            textoNome = view.findViewById(R.id.textoSala)
            textoEndereco = view.findViewById(R.id.textoEndereco)
            textoTelefone = view.findViewById(R.id.textoTelefone)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudioPlaceAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estudio, parent, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textoNome.text = listaEstudios.get(position).nome
        holder.textoEndereco.text = listaEstudios.get(position).endereco
        holder.textoTelefone.text = listaEstudios.get(position).telefone
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount() = listaEstudios.size

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}