package com.example.tcc

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.tcc.model.Estudio
import com.example.tcc.model.Musico
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CadastroEstudioActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    lateinit var mDatabase : DatabaseReference
    var mAuth = FirebaseAuth.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_estudio)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun cadastrarEstudio(view: View?) {
        val textoNomeCadastro = findViewById(R.id.textoNomeCadastroEstudio) as EditText
        val textoEmailCadastro = findViewById(R.id.textoEmailCadastroEstudio) as EditText
        val textoSenhaCadastro = findViewById(R.id.textoSenhaCadastroEstudio) as EditText

        val nome = textoNomeCadastro.text.toString()
        val email = textoEmailCadastro.text.toString()
        val senha = textoSenhaCadastro.text.toString()

        if (!email.isEmpty() && !senha.isEmpty() && !nome.isEmpty()) {

            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val uid = user!!.uid
                    mDatabase.child(uid).child("Name").setValue(nome)
                }
            })

            val estudio = Estudio(nome, email, senha)

            db.collection("estudios")
                .add(estudio)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, "Erro ao cadastrar estúdio :(", Toast.LENGTH_LONG).show()
        }

        finish()
    }

    fun cancelarCadastroEstudio(view: View?) {
        finish()
    }
}