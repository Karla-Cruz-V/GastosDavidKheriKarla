package edu.itesm.gastos.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import edu.itesm.gastos.dao.GastoDao
import edu.itesm.gastos.database.GastosDB
import edu.itesm.gastos.entities.Gasto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivityViewModel : ViewModel(){
     var liveData: MutableLiveData<List<Gasto>>
    init {
        liveData = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<List<Gasto>>{
        return liveData
    }

    fun getGastos(gastoDao: GastoDao){
        CoroutineScope(Dispatchers.IO).launch{
            for(i in 0..100){
                gastoDao.insertGasto(Gasto(0,"Gasto ${i}", Random.nextDouble()*100))
            }
            liveData.postValue(gastoDao.getAllGastos())
        }


    }
}