package com.example.tcc

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.adapter.EstudioPlaceAdapter
import com.example.tcc.model.EstudioPlace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MusicoActivity : AppCompatActivity(), EstudioPlaceAdapter.OnItemClickListener {
    private val REQ_CADASTRO = 1;
    private val REQ_DETALHE = 2;
    private var listaEstudios: ArrayList<EstudioPlace> = ArrayList()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EstudioPlaceAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musico)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "Meu Estúdio - Estúdios"

        viewManager = LinearLayoutManager(this)
        viewAdapter = EstudioPlaceAdapter(listaEstudios)
        viewAdapter.onItemClickListener = this

        listarEstudios()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
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
        val it = Intent(this, EstudioInfos2Activity::class.java)
        val estudio = listaEstudios.get(position)
        it.putExtra("estudio", estudio)
        startActivityForResult(it, REQ_DETALHE)
    }

    fun listarEstudios() {
        // listar do firebase
        db.collection("estudios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var estudioPlace = EstudioPlace(
                        document.data["nome"] as String?,
                        document.data["endereco"] as String?,
                        document.data["telefone"] as String?,
                        document.data["key"] as String?
                    )
                    Log.d(ContentValues.TAG, "estudioPlace = ${estudioPlace.toString()}")
                    listaEstudios.add(estudioPlace)
//                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                }
                viewAdapter.notifyDataSetChanged()
                Log.d(ContentValues.TAG, "estudioPlace = ${listaEstudios.toString()}")
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}