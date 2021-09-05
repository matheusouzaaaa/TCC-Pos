package com.example.tcc

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.tcc.model.Estudio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.hashMapOf as hashMapOf

class CadastroEstudioActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_estudio)
        auth= FirebaseAuth.getInstance()
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
            val estudio = Estudio(nome, email, senha)

            Log.d(ContentValues.TAG, "estudio nome  ${nome.toString()}")
            Log.d(ContentValues.TAG, "estudio email ${email.toString()}")
            Log.d(ContentValues.TAG, "estudio senha ${senha.toString()}")

            auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d(ContentValues.TAG, "task is successful")
                    var uid = task.result?.user?.uid;
                    Log.d(ContentValues.TAG, "user uid $uid")
                    saveFirestore(task.result?.user, estudio)
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
            Toast.makeText(this, "Preencha todos os dados :(", Toast.LENGTH_LONG).show()
        }
        finish()
    }

    private fun saveFirestore(user: FirebaseUser?, estudio: Estudio?) {
        user?.let {
            val dados = hashMapOf(
                "nome" to estudio?.nome.toString(),
                "email" to estudio?.email.toString(),
                "senha" to estudio?.senha.toString(),
                "key" to user.uid.toString(),
                "estudio" to true,
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

    fun cancelarCadastroEstudio(view: View?) {
        finish()
    }
}