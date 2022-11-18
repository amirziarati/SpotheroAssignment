package com.spothero.challenge.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Spot(
    @PrimaryKey val id: Int,
    @Embedded val address: Address,
    val description: String,
    val distance: String,
    @SerialName("facility_photo") var facilityPhoto: String,
    val price: Long
) : java.io.Serializable