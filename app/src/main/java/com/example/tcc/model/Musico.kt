package com.example.tcc.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Musico(val nome: String?=null, val email: String?=null, val senha: String?=null, @Exclude val key: String? = null):Serializable