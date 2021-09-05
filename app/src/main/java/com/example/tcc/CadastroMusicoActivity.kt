package com.example.tcc

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tcc.model.Estudio
import com.example.tcc.model.Musico
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CadastroMusicoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_musico)
        auth= FirebaseAuth.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun cadastrarMusico(view: View?) {
        val textoNomeCadastro = findViewById(R.id.textoNomeCadastroMusico) as EditText
        val textoEmailCadastro = findViewById(R.id.textoEmailCadastroMusico) as EditText
        val textoSenhaCadastro = findViewById(R.id.textoSenhaCadastroMusico) as EditText

        val nome = textoNomeCadastro.text.toString()
        val email = textoEmailCadastro.text.toString()
        val senha = textoSenhaCadastro.text.toString()

        if (!email.isEmpty() && !senha.isEmpty() && !nome.isEmpty()) {
            val musico = Musico(nome, email, senha)

            Log.d(ContentValues.TAG, "musico nome  ${nome.toString()}")
            Log.d(ContentValues.TAG, "musico email ${email.toString()}")
            Log.d(ContentValues.TAG, "musico senha ${senha.toString()}")

            auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d(ContentValues.TAG, "task is successful")
                    var uid = task.result?.user?.uid;
                    Log.d(ContentValues.TAG, "user uid $uid")
                    saveFirestore(task.result?.user, musico)
                }else if(task.isComplete){
                    Log.d(ContentValues.TAG, "task is complete")
                }else if(task.isCanceled){
                    Log.d(ContentValues.TAG, "task is canceled")
                }else{
                    Log.d(ContentValues.TAG, "task else")
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else {
            Toast.makeText(this, "Erro ao cadastrar músico :(", Toast.LENGTH_LONG).show()
        }
        finish()
    }

    private fun saveFirestore(user: FirebaseUser?, musico: Musico?) {
        user?.let {
            val dados = hashMapOf(
                "nome" to musico?.nome.toString(),
                "email" to musico?.email.toString(),
                "senha" to musico?.senha.toString(),
                "key" to user.uid.toString(),
                "estudio" to false,
            )
            db.collection("usuarios").document(it.uid).set(dados)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    fun cancelarCadastroMusico(view: View?) {
        finish()
    }
}