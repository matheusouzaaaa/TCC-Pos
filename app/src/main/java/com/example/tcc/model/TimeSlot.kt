package com.example.tcc.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class TimeSlot(val slot: String?=null, val sala_id: String?=null, val user_id: String?=null, val date: String?=null, var marcado: String?=null): Serializable