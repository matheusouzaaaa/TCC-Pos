package com.example.tcc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.R
import com.example.tcc.model.Sala

class EstudioInfoAdapter(private var listaSalas: ArrayList<Sala>) : RecyclerView.Adapter<EstudioInfoAdapter.MyViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textoSala: TextView
        var textoPreco: TextView
        var textoInformacoes: TextView

        init {
            textoSala = view.findViewById(R.id.textoSala)
            textoPreco = view.findViewById(R.id.textoEndereco)
            textoInformacoes = view.findViewById(R.id.textoTelefone)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudioInfoAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estudio_info, parent, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textoSala.text = listaSalas.get(position).nome
        holder.textoPreco.text = listaSalas.get(position).preco.toString()
        holder.textoInformacoes.text = listaSalas.get(position).informacoes
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount() = listaSalas.size

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}