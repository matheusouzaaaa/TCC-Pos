package com.example.tcc.adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.Common.Common
import com.example.tcc.ConfirmaCadastroHorarioActivity
import com.example.tcc.R
import com.example.tcc.SalaInfosActivity
import com.example.tcc.model.TimeSlot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyTimeSlotAdapter(private var context: Context, private var timeSlotList: ArrayList<TimeSlot>): RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txt_time_slot: TextView
        var txt_time_slot_description: TextView
        var card_time_slot: CardView

        init {
            card_time_slot = view.findViewById(R.id.card_time_slot)
            txt_time_slot = view.findViewById(R.id.txt_time_slot)
            txt_time_slot_description = view.findViewById(R.id.txt_time_slot_description)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTimeSlotAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_slot, parent, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_time_slot.text = Common.convertTimeSlotToString(position).toString()
        Log.d(ContentValues.TAG, "MARCADO = ${timeSlotList[position].marcado}")
        if (timeSlotList[position].marcado == "true"){
            Log.d(ContentValues.TAG, "entrou no if no $position")
            holder.txt_time_slot_description.text = "Ocupado"
            holder.txt_time_slot_description.setTextColor(Color.WHITE)
            holder.txt_time_slot.setTextColor(Color.WHITE)
            holder.card_time_slot.setBackgroundColor(Color.DKGRAY)
        }else{
            Log.d(ContentValues.TAG, "entrou no else no $position")
            holder.txt_time_slot_description.text = "Disponível"
            holder.txt_time_slot_description.setTextColor(Color.GRAY)
            holder.txt_time_slot.setTextColor(Color.GRAY)
            holder.card_time_slot.setBackgroundColor(Color.WHITE)
        }
        holder.itemView.setOnClickListener {
            Log.d(ContentValues.TAG, timeSlotList.toString())
            Log.d(ContentValues.TAG, position.toString())
            val horario = timeSlotList.get(position)
            val intent = Intent(context, ConfirmaCadastroHorarioActivity::class.java)
            intent.putExtra("horario", horario)

            val horarioDescrito = Common.convertTimeSlotToString(position).toString()
            val params = Bundle()
            params.putString("horarioDescrito", horarioDescrito as String)
            intent.putExtras(params)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return Common.TIME_SLOT_TOTAL;
    }

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}