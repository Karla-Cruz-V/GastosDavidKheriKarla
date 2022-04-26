package edu.itesm.gastos.dao

import androidx.room.*
import edu.itesm.gastos.entities.Gasto

@Dao
interface GastoDao{
    @Query("SELECT * from Gasto")
    suspend fun getAllGastos(): List<Gasto>?

    @Query("SELECT SUM(monto) from Gasto")
    suspend fun getTotalGastos(): Double?

    @Query("DELETE from Gasto")
    suspend fun deleteGastos()

    @Insert
    suspend fun createGasto(gasto: Gasto)
}

