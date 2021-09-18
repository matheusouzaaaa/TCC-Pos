package com.example.tcc

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.tcc.model.Estudio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth

        val docRef = db.collection("usuarios").document(auth.currentUser!!.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    if(document.data?.get("estudio") == true){
                        val intent = Intent(this, EstudioActivity::class.java)
                        val params = Bundle()
                        params.putString("nome", document.data?.get("nome") as String)
                        params.putString("email", document.data?.get("email") as String)
                        params.putString("key", document.data?.get("key") as String)
                        params.putBoolean("estudio", document.data?.get("estudio") as Boolean)
                        params.putString("senha", document.data?.get("senha") as String)
                        intent.putExtras(params)
                        startActivity(intent)
                    }else{
//                        Log.d(ContentValues.TAG, "Musico: ${document.data}")
                        val intent = Intent(this, MusicoActivity::class.java)
                        val params = Bundle()
                        params.putString("nome", document.data?.get("nome") as String)
                        params.putString("email", document.data?.get("email") as String)
                        params.putString("key", document.data?.get("key") as String)
                        params.putBoolean("estudio", document.data?.get("estudio") as Boolean)
                        params.putString("senha", document.data?.get("senha") as String)
                        intent.putExtras(params)
                        startActivity(intent)
                    }
//                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
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
}