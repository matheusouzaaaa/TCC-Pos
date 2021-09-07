package com.example.tcc

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.R.id.recyclerViewSalas
import com.example.tcc.adapter.EstudioPlaceAdapter
import com.example.tcc.databinding.ActivityMainBinding
import com.example.tcc.model.Estudio
import com.example.tcc.model.EstudioPlace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EstudioActivity : AppCompatActivity(), EstudioPlaceAdapter.OnItemClickListener {
    private val REQ_CADASTRO = 1;
    private val REQ_DETALHE = 2;
    private var listaEstudios: ArrayList<EstudioPlace> = ArrayList()
    private var posicaoAlterar = -1

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EstudioPlaceAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudio)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewManager = LinearLayoutManager(this)
        viewAdapter = EstudioPlaceAdapter(listaEstudios)
        viewAdapter.onItemClickListener = this

        listarEstudios(intent.getStringExtra("key"))

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

    private fun logout() {
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
    }

    override fun onItemClicked(view: View, position: Int) {
        val it = Intent(this, EstudioInfosActivity::class.java)
        val estudio = listaEstudios.get(position)
        it.putExtra("estudio", estudio)
        startActivityForResult(it, REQ_DETALHE)
    }

    fun abrirFormulario(view: View) {
        val it = Intent(this, EstudioDetalheActivity::class.java)
        startActivityForResult(it, REQ_CADASTRO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CADASTRO) {
            if (resultCode == Activity.RESULT_OK) {
                val estudioPlace = data?.getSerializableExtra("estudioPlace") as EstudioPlace
                auth= FirebaseAuth.getInstance()

                var ref:DocumentReference = db.collection("estudios").document()
                var docId:String = ref.id.toString()

                val dados = hashMapOf(
                    "nome" to estudioPlace?.nome.toString(),
                    "endereco" to estudioPlace?.endereco.toString(),
                    "telefone" to estudioPlace?.telefone.toString(),
                    "user_id" to auth.currentUser?.uid.toString(),
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

    fun listarEstudios(user_id: String?) {
        // listar do firebase
        db.collection("estudios").whereEqualTo("user_id", user_id).addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {

                        var estudioPlace = EstudioPlace(
                            dc.document.toObject(EstudioPlace::class.java).nome,
                            dc.document.toObject(EstudioPlace::class.java).endereco,
                            dc.document.toObject(EstudioPlace::class.java).telefone,
                            dc.document.id
                        )
                        listaEstudios.add(estudioPlace)
                    }
                }

                viewAdapter.notifyDataSetChanged()
            }

        })
    }
}