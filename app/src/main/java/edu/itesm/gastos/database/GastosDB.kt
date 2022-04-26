package edu.itesm.gastos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.itesm.gastos.dao.GastoDao
import edu.itesm.gastos.entities.Gasto

@Database(entities = [Gasto::class], version = 1)
abstract class GastosDB: RoomDatabase(){
    abstract fun gastoDao(): GastoDao
}




