package com.example.tcc

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.Common.ITimeSlotLoadListener
import com.example.tcc.adapter.MyTimeSlotAdapter
import com.example.tcc.model.Sala
import com.example.tcc.model.TimeSlot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SalaInfosActivity : AppCompatActivity(), ITimeSlotLoadListener, MyTimeSlotAdapter.OnItemClickListener {
    private val REQ_CADASTRO = 1;
    private val REQ_DETALHE = 2;
    private var listaHorarios: ArrayList<TimeSlot> = ArrayList()
    private var posicaoAlterar = -1

    private lateinit var iTimeSlotLoadListener: ITimeSlotLoadListener
    private var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd_MM_yyyy")

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyTimeSlotAdapter
    private lateinit var viewManager: GridLayoutManager
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        // info sala
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala_infos)

        val intent = intent
        val sala = intent.getSerializableExtra("sala") as Sala

//        Log.d(ContentValues.TAG, sala.toString())
        val user = Firebase.auth.currentUser
//        Log.d(ContentValues.TAG, "USER: ${ user?.uid.toString() }")

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

        // informações dos horários
        viewManager = GridLayoutManager(this,2)
        viewAdapter = MyTimeSlotAdapter(this, listaHorarios)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHorarios).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        iTimeSlotLoadListener = this

        // inicia horarios

        // set data atual
        var selected_date = Calendar.getInstance().apply {
            add(Calendar.DATE,0)
        }

        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.DATE, 0)

        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.DATE, 30)

        val horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(3)
            .mode(HorizontalCalendar.Mode.DAYS)
            .defaultSelectedDate(startDate)
            .build()

        // lista os horários de hoje da sala
        listarHorarios(sala.key.toString(), simpleDateFormat.format(selected_date.time))

        // se houver mudança de datas, recarrega os horários
        horizontalCalendar.calendarListener = object: HorizontalCalendarListener(){
            override fun onDateSelected(date: Calendar?, position: Int) {
                if(selected_date.timeInMillis != date?.timeInMillis){
                    selected_date = date // this code will not load again if u selected new day with day selected
                    Log.d(ContentValues.TAG, simpleDateFormat.format(date?.time))
                    listarHorarios(sala.key.toString(), simpleDateFormat.format(date?.time))
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

    fun listarHorarios(sala_id: String?, date: String) {
        listaHorarios.clear();
        val user = Firebase.auth.currentUser
        for (i in 0..7) {
            var horario = TimeSlot(
                i.toString(),
                sala_id.toString(),
                user?.uid.toString(),
                date.toString(),
                "false"
            )
            listaHorarios.add(horario)
        }

        // listar do firebase
        db.collection("salas").document(sala_id.toString()).collection(date).addSnapshotListener { value, error ->
            if (error != null) {
                Log.d(ContentValues.TAG, "nao carregou os dados")
                Log.e("Firestore Error", error.message.toString())
                onTimeSlotLoadFailed(error.message.toString())
            }

            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    Log.d(ContentValues.TAG, "carregou")
                    listaHorarios?.find { it.slot == dc.document.toObject(TimeSlot::class.java).slot.toString() }?.marcado = "true"
                }else{
                    Log.d(ContentValues.TAG, "entrou no else")
                }
            }

            onTimeSlotLoadSuccess(listaHorarios)
            viewAdapter.notifyDataSetChanged()
        }
    }

    override fun onTimeSlotLoadSuccess(timeSlotList: java.util.ArrayList<TimeSlot>?) {
        val listaHorarios: ArrayList<TimeSlot> = timeSlotList!!
        viewManager = GridLayoutManager(this,2)
        viewAdapter = MyTimeSlotAdapter(this, listaHorarios)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHorarios).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
    }

    override fun onTimeSlotLoadFailed(message: String?) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun onTimeSlotLoadEmpty() {
        Log.d(ContentValues.TAG, "onTimeSlotLoadEmpty")
        viewManager = GridLayoutManager(this,2)
        viewAdapter = MyTimeSlotAdapter(this, listaHorarios)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHorarios).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ConfirmaCadastroHorarioActivity.RESULT_EDIT) {
            val horario = data?.getSerializableExtra("horario") as TimeSlot

            Log.d(ContentValues.TAG, "horario = $horario")
            auth = FirebaseAuth.getInstance()

            var ref: DocumentReference = db.collection("salas").document(horario.sala_id.toString()).collection(horario.date.toString()).document(horario.slot.toString())

            val dados = hashMapOf(
                "slot" to horario?.slot.toString(),
                "date" to horario?.date.toString(),
                "sala_id" to horario?.sala_id.toString(),
                "user_id" to auth.currentUser?.uid.toString(),
            )

            ref.set(dados)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

            viewAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Horário agendado com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onItemClick(position: Int) {
//        Toast.makeText(this,"HORARIO $position CLICADO",Toast.LENGTH_SHORT).show()
//        val horario = listaHorarios[position]
//        Log.d(ContentValues.TAG, "horario = ${horario.toString()}")
//        auth = FirebaseAuth.getInstance()
//
//        var ref: DocumentReference = db.collection("salas").document(horario.sala_id.toString()).collection(horario.date.toString()).document(horario.slot.toString())
//
//        val dados = hashMapOf(
//            "slot" to horario?.slot.toString(),
//            "date" to horario?.date.toString(),
//            "sala_id" to horario?.sala_id.toString(),
//            "user_id" to auth.currentUser?.uid.toString(),
//        )
//
//        ref.set(dados)
//            .addOnSuccessListener {
//                Log.d(ContentValues.TAG, "DocumentSnapshot added")
//            }
//            .addOnFailureListener { e ->
//                Log.w(ContentValues.TAG, "Error adding document", e)
//            }
//
//        viewAdapter.notifyDataSetChanged()
//        Toast.makeText(this, "Horário agendado com sucesso!", Toast.LENGTH_SHORT).show()
//    }

    fun cadastrarTeste(view: View){
        auth = FirebaseAuth.getInstance()
        ///salas/6jMrjDbbsd90qKJETNJp/18_09_2021/5

        var ref: DocumentReference = db.collection("salas").document("6jMrjDbbsd90qKJETNJp").collection("20_09_2021").document("3")

        val dados = hashMapOf(
            "slot" to "3",
            "date" to "20_09_2021",
            "sala_id" to "6jMrjDbbsd90qKJETNJp",
            "user_id" to auth.currentUser?.uid.toString(),
        )

        ref.set(dados)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

        viewAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Horário agendado com sucesso!", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(view: View, position: Int) {
        Log.d(ContentValues.TAG, "hello motherfucker")
    }
}