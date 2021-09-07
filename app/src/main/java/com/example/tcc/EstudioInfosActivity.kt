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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.adapter.EstudioInfoAdapter
import com.example.tcc.adapter.EstudioPlaceAdapter
import com.example.tcc.model.EstudioPlace
import com.example.tcc.model.Sala
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EstudioInfosActivity : AppCompatActivity(), EstudioInfoAdapter.OnItemClickListener {

    private val REQ_CADASTRO = 1;
    private val REQ_DETALHE = 2;

    private lateinit var recyclerViewSalas: RecyclerView
    private lateinit var viewAdapter: EstudioInfoAdapter
    private var listaSalas: ArrayList<Sala> = ArrayList()
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val db = Firebase.firestore

    private var estudio_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudio_infos)

        val intent = intent
        val estudio = intent.getSerializableExtra("estudio") as EstudioPlace

        Log.d(ContentValues.TAG, estudio.toString())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewManager = LinearLayoutManager(this)
        viewAdapter = EstudioInfoAdapter(listaSalas)
        viewAdapter.onItemClickListener = this

        listarSalas(estudio.key.toString())

        estudio_id = estudio.key.toString()

        recyclerViewSalas = findViewById<RecyclerView>(R.id.recyclerViewSalas).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        var textoSala = findViewById<TextView>(R.id.textoSala)
        var textoEndereco = findViewById<TextView>(R.id.textoEndereco)
        var textoTelefone = findViewById<TextView>(R.id.textoTelefone)

        textoSala.text = estudio.nome.toString()
        textoEndereco.text = estudio.endereco.toString()
        textoTelefone.text = estudio.telefone.toString()

    }

    override fun onItemClicked(view: View, position: Int) {
        TODO("Not yet implemented")
    }

    fun listarSalas(estudio_id: String?) {
        // listar do firebase
        db.collection("salas").whereEqualTo("estudio_id", estudio_id).addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        Log.d(ContentValues.TAG, dc.document.toString())

                        var sala = Sala(
                            dc.document.toObject(Sala::class.java).nome,
                            dc.document.toObject(Sala::class.java).preco,
                            dc.document.toObject(Sala::class.java).informacoes,
                            dc.document.id
                        )
                        listaSalas.add(sala)
                    }
                }

                viewAdapter.notifyDataSetChanged()
            }

        })
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

    fun abrirFormularioSala(view: View) {
        val it = Intent(this, CadastroSalaActivity::class.java)
        startActivityForResult(it, REQ_CADASTRO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CADASTRO) {
            if (resultCode == Activity.RESULT_OK) {
                val sala = data?.getSerializableExtra("sala") as Sala

                var ref: DocumentReference = db.collection("salas").document()
                var docId:String = ref.id.toString()

                val dados = hashMapOf(
                    "nome" to sala?.nome.toString(),
                    "preco" to sala?.preco?.toFloat(),
                    "informacoes" to sala?.informacoes.toString(),
                    "estudio_id" to estudio_id,
                    "key" to docId,
                )

                ref.set(dados)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "DocumentSnapshot added")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }

                viewAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}