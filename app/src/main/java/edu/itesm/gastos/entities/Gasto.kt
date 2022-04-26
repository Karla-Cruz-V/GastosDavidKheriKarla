package edu.itesm.gastos.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Gasto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
    val monto: Double
)

