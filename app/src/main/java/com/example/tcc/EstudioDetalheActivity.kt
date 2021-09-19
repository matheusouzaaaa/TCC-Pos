package com.example.tcc

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.tcc.model.EstudioPlace
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EstudioDetalheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudio_detalhe)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Cadastrar Estúdio"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    fun cadastrarEstudio(view: View?) {
        val textoNomeCadastro = findViewById(R.id.textoNomeCadastro) as EditText
        val textoEnderecoCadastro = findViewById(R.id.textoEnderecoCadastro) as EditText
        val textoTelefoneCadastro = findViewById(R.id.textoTelefoneCadastro) as EditText

        val nome = textoNomeCadastro.text.toString()
        val endereco = textoEnderecoCadastro.text.toString()
        val telefone = textoTelefoneCadastro.text.toString()
        val estudioPlace = EstudioPlace(nome, endereco, telefone)
        val it = Intent().apply {
            putExtra("estudioPlace", estudioPlace)
        }
        setResult(Activity.RESULT_OK, it)

        finish()
    }

    fun cancelarCadastro(view: View?) {
        finish()
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
}