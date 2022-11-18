package com.spothero.challenge.data.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val street: String,
    val city: String,
    val state: String
)