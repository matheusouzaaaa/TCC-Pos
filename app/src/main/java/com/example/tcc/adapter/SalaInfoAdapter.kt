package com.example.tcc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.R
import com.example.tcc.model.Horario
import com.example.tcc.model.Sala

class SalaInfoAdapter(private var listaHorarios: ArrayList<Horario>) : RecyclerView.Adapter<SalaInfoAdapter.MyViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaInfoAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horario_info, parent, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textoSala.text = listaHorarios.get(position).nome
        holder.textoPreco.text = listaHorarios.get(position).preco.toString()
        holder.textoInformacoes.text = listaHorarios.get(position).informacoes
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount() = listaHorarios.size

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}