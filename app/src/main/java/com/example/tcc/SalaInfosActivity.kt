package com.example.tcc

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.adapter.EstudioPlaceAdapter
import com.example.tcc.adapter.MyTimeSlotAdapter
import com.example.tcc.adapter.SalaInfoAdapter
import com.example.tcc.model.EstudioPlace
import com.example.tcc.model.Horario
import com.example.tcc.model.Sala
import com.example.tcc.model.TimeSlot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener


class SalaInfosActivity : AppCompatActivity(), SalaInfoAdapter.OnItemClickListener{
    private val REQ_CADASTRO = 1;
    private val REQ_DETALHE = 2;
    private var listaHorarios: ArrayList<TimeSlot> = ArrayList()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyTimeSlotAdapter
    private lateinit var viewManager: GridLayoutManager
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala_infos)

        val intent = intent
        val sala = intent.getSerializableExtra("sala") as Sala

        Log.d(ContentValues.TAG, sala.toString())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        var textoNomeSala = findViewById<TextView>(R.id.textoNomeSala)
        var textoPreco = findViewById<TextView>(R.id.textoPreco)
        var textoInformacoes = findViewById<TextView>(R.id.textoInformacoes)

        textoNomeSala.text = sala.nome.toString()
        textoPreco.text = sala.preco.toString()
        textoInformacoes.text = sala.informacoes.toString()

        viewManager = GridLayoutManager(this,2)
        viewAdapter = MyTimeSlotAdapter(listaHorarios)

//        listarHorarios(intent.getStringExtra("key"))

        var selected_date = Calendar.getInstance()
        selected_date.add(Calendar.DATE,0) // init current date

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHorarios).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
        /* starts before 1 month from now */
        /* starts before 1 month from now */
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.DATE, 0)

        /* ends after 1 month from now */

        /* ends after 1 month from now */
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.DATE, 30)

        val horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(3)
            .mode(HorizontalCalendar.Mode.DAYS)
            .defaultSelectedDate(startDate)
            .build()

                    horizontalCalendar.calendarListener = object: HorizontalCalendarListener(){
                        override fun onDateSelected(date: Calendar?, position: Int) {
                            if(selected_date.timeInMillis != date?.timeInMillis){
                                selected_date = date // this code will not load again if u selected new day with day selected
                            }
                        }
                    }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout -> {
            logout()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        Firebase.auth.signOut()
        val intent = Intent (this, LoginActivity::class.java)
        this.startActivity(intent)
    }

    override fun onItemClicked(view: View, position: Int) {
        TODO("Not yet implemented")
    }

//    fun listarHorarios(sala_id: String?) {
//        // listar do firebase
//        db.collection("horarios").whereEqualTo("sala_id", sala_id).addSnapshotListener(object : EventListener<QuerySnapshot> {
//            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                if (error != null) {
//                    Log.e("Firestore Error", error.message.toString())
//                }
//
//                for (dc: DocumentChange in value?.documentChanges!!) {
//                    if (dc.type == DocumentChange.Type.ADDED) {
//
//                        var horario = Horario(
//                            dc.document.toObject(Horario::class.java).nome,
//                            dc.document.toObject(Horario::class.java).preco,
//                            dc.document.toObject(Horario::class.java).informacoes,
//                            dc.document.id
//                        )
//                        listaHorarios.add(horario)
//                    }
//                }
//
//                viewAdapter.notifyDataSetChanged()
//            }
//
//        })
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQ_CADASTRO) {
//            if (resultCode == Activity.RESULT_OK) {
//                val horario = data?.getSerializableExtra("horario") as Horario
//                auth= FirebaseAuth.getInstance()
//
//                var ref: DocumentReference = db.collection("horarios").document()
//                var docId:String = ref.id.toString()
//
//                val dados = hashMapOf(
//                    "nome" to horario?.nome.toString(),
//                    "endereco" to horario?.preco.toString(),
//                    "telefone" to horario?.informacoes.toString(),
//                    "user_id" to auth.currentUser?.uid.toString(),
//                    "key" to docId,
//                )
//
//                ref.set(dados)
//                    .addOnSuccessListener {
//                        Log.d(ContentValues.TAG, "DocumentSnapshot added")
//                    }
//                    .addOnFailureListener { e ->
//                        Log.w(ContentValues.TAG, "Error adding document", e)
//                    }
//
//                viewAdapter.notifyDataSetChanged()
//                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
}