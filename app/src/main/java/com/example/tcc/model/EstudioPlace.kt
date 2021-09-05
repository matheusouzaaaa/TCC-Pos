package com.example.tcc.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class EstudioPlace(val nome: String?=null, val endereco: String?=null, val telefone: String?=null, @Exclude val key: String? = null):Serializable