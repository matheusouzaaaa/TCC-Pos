package com.example.tcc.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Horario(val nome: String?=null, val preco: Float?=null, val informacoes: String?=null, @Exclude val estudio_id: String? = null): Serializable
