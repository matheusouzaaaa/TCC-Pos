package com.example.tcc.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Sala(val nome: String?=null, val preco: Float?=null, val informacoes: String?=null, @Exclude val key: String? = null): Serializable