package com.example.tcc

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.tcc.model.EstudioPlace
import com.example.tcc.model.Sala
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CadastroSalaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_sala)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Cadastrar Sala"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun cadastrarSala(view: View?) {
        val textoNomeSalaCadastro = findViewById(R.id.textoNomeSalaCadastro) as EditText
        val textoPrecoSalaCadastro = findViewById(R.id.textoPrecoSalaCadastro) as EditText
        val textoInfoSalaCadastro = findViewById(R.id.textoInfoSalaCadastro) as EditText

        val nome = textoNomeSalaCadastro.text.toString()
        val preco = textoPrecoSalaCadastro.text.toString().toFloat()
        val info = textoInfoSalaCadastro.text.toString()
        val sala = Sala(nome, preco, info)
        val it = Intent().apply {
            putExtra("sala", sala)
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