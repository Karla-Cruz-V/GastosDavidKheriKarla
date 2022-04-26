package edu.itesm.gastos.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import edu.itesm.gastos.R
import edu.itesm.gastos.dao.GastoDao
import edu.itesm.gastos.database.GastosDB
import edu.itesm.gastos.databinding.ActivityMainBinding
import edu.itesm.gastos.entities.Gasto
import edu.itesm.gastos.mvvm.MainActivityViewModel
import edu.itesm.perros.adapter.GastosAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var gastoDao: GastoDao
    private lateinit var  gastos: List<Gasto>
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GastosAdapter
    private lateinit var viewModel : MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Room.databaseBuilder(this@MainActivity, GastosDB::class.java, "gastos").build()
        gastoDao = db.gastoDao()
        initRecycler()
        initViewModel()
        initButtonsOnClickListeners()
    }

    private fun initButtonsOnClickListeners() {
        binding.addGastoButton.setOnClickListener {
            handleAddButton()
        }

        binding.gastosTotalButton.setOnClickListener {
            handleTotalButton()
        }

        binding.deleteAllButton.setOnClickListener {
            handleDeleteButton()
        }
    }

    private fun handleDeleteButton() {
        viewModel.deleteData(gastoDao) {
            this.runOnUiThread {
                Toast.makeText(this, "Gastos eliminados correctamente", Toast.LENGTH_SHORT).show()
                viewModel.getGastos(gastoDao)
            }
        }
    }

    private fun handleAddButton() {
        showAddGastoDialog()
    }

    private fun addGasto(description: String, amount: Double) {
        viewModel.addGasto(gastoDao, Gasto(0, description, amount)) {
            this.runOnUiThread {
                Toast.makeText(this, "Gasto agregado correctamente", Toast.LENGTH_SHORT).show()
                viewModel.getGastos(gastoDao)
            }
        }
    }

    private fun showAddGastoDialog() {
        //creamos un dialogo de texto en vez de otro fragment :) nos ayudamos de internet para hacerlo
        //se nos hizo más bonito que los fragments :)
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Añade un nuevo gasto")

        val exView = LinearLayout(this)
        exView.orientation = LinearLayout.VERTICAL

        val descriptionInput = EditText(this)
        descriptionInput.hint = "Concepto del gasto"
        descriptionInput.inputType = InputType.TYPE_CLASS_TEXT
        exView.addView(descriptionInput)

        val amountInput = EditText(this)
        amountInput.hint = "Ingresa la cantidad gastada"
        amountInput.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        exView.addView(amountInput)

        builder.setView(exView)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDouble()
            addGasto(description, amount)
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        builder.show()
    }

    private fun handleTotalButton() {
        viewModel.getTotalGastos(gastoDao) {
            this.runOnUiThread {
                if(it == null) {
                    Toast.makeText(this, "No hay gastos aún", Toast.LENGTH_SHORT).show()
                    return@runOnUiThread
                }
                Toast.makeText(this, "Total gastado: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecycler(){
        gastos = mutableListOf<Gasto>()
        adapter = GastosAdapter(gastos)
        binding.gastos.layoutManager = LinearLayoutManager(this)
        binding.gastos.adapter = adapter
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getLiveDataObserver().observe(this, Observer {
            adapter.setGastos(it)
            adapter.notifyDataSetChanged()
        })
        viewModel.getGastos(gastoDao)
    }
}