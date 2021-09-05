package com.example.tcc.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Banda(val nome: String?=null, val genero: String?=null, @Exclude val key: String? = null):Serializable