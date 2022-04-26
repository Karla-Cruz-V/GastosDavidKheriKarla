package edu.itesm.gastos.dao

import androidx.room.*
import edu.itesm.gastos.entities.Gasto

@Dao
interface GastoDao{
    @Query("SELECT * from Gasto")
    suspend fun getAllGastos(): List<Gasto>

    //"SELECT SUM(monto) from Gasto"

    @Insert
    suspend fun insertGasto(gasto: Gasto)
}

