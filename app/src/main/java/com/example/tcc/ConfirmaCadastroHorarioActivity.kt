package com.example.tcc

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.tcc.model.EstudioPlace
import com.example.tcc.model.TimeSlot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConfirmaCadastroHorarioActivity : AppCompatActivity() {
    companion object {
        const val RESULT_EDIT = 1
    }

    private lateinit var date: TextView
    private lateinit var dateScreen: TextView
    private lateinit var slotScreen: TextView
    private lateinit var slot: TextView
    private lateinit var sala_id: TextView
    private lateinit var user_id: TextView
    private lateinit var marcado: TextView
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirma_cadastro_horario)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Confirmação de horário"

        val intent = intent
        val horario = intent.getSerializableExtra("horario") as TimeSlot
        val horarioDescrito = intent.getStringExtra("horarioDescrito");


        // ajustar campos na tela para o usuário confirmar


        dateScreen = findViewById<TextView>(R.id.dateScreen).apply {
            text = horario.date.toString().replace("_","/")
        }

        slotScreen = findViewById<TextView>(R.id.slotScreen).apply {
            text = horarioDescrito.toString()
        }

        date = findViewById<TextView>(R.id.date).apply {
            text = horario.date.toString()
        }

        slot = findViewById<TextView>(R.id.slot).apply {
            text = horario.slot.toString()
        }

        sala_id = findViewById<TextView>(R.id.sala_id).apply {
            text = horario.sala_id.toString()
        }

        user_id = findViewById<TextView>(R.id.user_id).apply {
            text = horario.user_id.toString()
        }

        marcado = findViewById<TextView>(R.id.marcado).apply {
            text = horario.marcado.toString()
        }

        Log.d(ContentValues.TAG, horario.toString())
    }

    fun confirmarHorario(v: View?) {
        val horario = TimeSlot(
            slot.text.toString(),
            sala_id.text.toString(),
            user_id.text.toString(),
            date.text.toString(),
            marcado.text.toString()
        )
        auth = FirebaseAuth.getInstance()
        ///salas/6jMrjDbbsd90qKJETNJp/18_09_2021/5

        var ref: DocumentReference = db.collection("salas").document(sala_id.text.toString()).collection(date.text.toString()).document(slot.text.toString())

        val dados = hashMapOf(
            "slot" to slot.text.toString(),
            "date" to date.text.toString(),
            "sala_id" to sala_id.text.toString(),
            "user_id" to auth.currentUser?.uid.toString(),
            "marcado" to "true",
        )

        ref.set(dados)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

//        viewAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Horário agendado com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun voltar(v: View?) {
        finish()
    }
}