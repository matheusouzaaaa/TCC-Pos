package com.example.tcc

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
    companion object {

        const val RESULT_EDIT = 1
        const val RESULT_DELETE = 2
    }

    private lateinit var textoNomeDetalhe: EditText
    private lateinit var textoEnderecoDetalhe: EditText
    private lateinit var textoTelefoneDetalhe: EditText
    private lateinit var keyFirebase: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudio_detalhe)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val intent = intent
        val estudioPlace = intent.getSerializableExtra("estudioPlace") as EstudioPlace
        textoNomeDetalhe = findViewById<EditText>(R.id.textoNomeDetalhe).apply {
            setText(estudioPlace.nome)
        }
        textoEnderecoDetalhe = findViewById<EditText>(R.id.textoEnderecoDetalhe).apply {
            setText(estudioPlace.endereco)
        }
        textoTelefoneDetalhe = findViewById<EditText>(R.id.textoTelefoneDetalhe).apply {
            setText(estudioPlace.telefone)
        }
        keyFirebase = findViewById<TextView>(R.id.key).apply {
            text = estudioPlace.key.toString()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun editarEstudioPlace(v: View?) {
        val estudioPlace = EstudioPlace(
            textoNomeDetalhe.text.toString(),
            textoEnderecoDetalhe.text.toString(),
            textoTelefoneDetalhe.text.toString(),
            keyFirebase.text.toString()
        )
        val data = Intent()
        data.putExtra("estudioPlace", estudioPlace)
        setResult(RESULT_EDIT, data)
        finish()
    }

    fun excluirEstudioPlace(v: View?) {
        val estudioPlace = EstudioPlace(
            textoNomeDetalhe.text.toString(),
            textoEnderecoDetalhe.text.toString(),
            textoTelefoneDetalhe.text.toString(),
            keyFirebase.text.toString()
        )
        val data = Intent()
        data.putExtra("estudioPlace", estudioPlace)
        setResult(RESULT_DELETE, data)
        finish()
    }

    fun voltar(v: View?) {
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