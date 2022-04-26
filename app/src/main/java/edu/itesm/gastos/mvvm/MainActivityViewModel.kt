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
import java.util.*
import kotlin.random.Random


class MainActivityViewModel : ViewModel(){
    var liveData: MutableLiveData<List<Gasto>>
    init {
        liveData = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<List<Gasto>>{
        return liveData
    }

    fun getGastos(gastoDao: GastoDao) {
        CoroutineScope(Dispatchers.IO).launch{
            liveData.postValue(gastoDao.getAllGastos())
        }
    }

    fun addGasto(gastoDao: GastoDao, gasto: Gasto, onCompletionHandler: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            gastoDao.createGasto(gasto)
            onCompletionHandler()
        }
    }

    fun deleteData(gastoDao: GastoDao, onCompletionHandler: () -> Unit) {
        val coroutine = CoroutineScope(Dispatchers.IO).launch {
            gastoDao.deleteGastos()
        }
        coroutine.invokeOnCompletion {
            onCompletionHandler()
        }
    }

    fun getTotalGastos(gastoDao: GastoDao, callBack: (total: Double?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            callBack(gastoDao.getTotalGastos())
        }
    }
}