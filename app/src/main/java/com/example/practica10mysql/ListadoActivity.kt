package com.example.practica10mysql
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica10mysql.Models.Vehicle
import com.example.practica10mysql.Network.RetrofitInstance
import kotlinx.coroutines.launch

class ListadoActivity : AppCompatActivity() {

    private lateinit var vehiculosRecyclerView: RecyclerView
    private lateinit var adapter: VehiculoAdapter
    private val vehiculosList = mutableListOf<Vehicle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)

        vehiculosRecyclerView = findViewById(R.id.vehiculosRecyclerView)
        vehiculosRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VehiculoAdapter(vehiculosList)
        vehiculosRecyclerView.adapter = adapter

        cargarVehiculos()
    }

    private fun cargarVehiculos() {
        //call retrofit
        lifecycleScope.launch {
            try{
                val response = RetrofitInstance.api.getVehicles()
                if(response.isNotEmpty()){
                    vehiculosList.clear()
                    vehiculosList.addAll(response)
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@ListadoActivity, "Ocurrió un error al obtener los vehículos!",Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                Log.d("GetVehiclesError", e.message.toString())
                Toast.makeText(this@ListadoActivity, "Ocurrió un error al obtener los vehículos!",Toast.LENGTH_LONG).show()
            }
        }

    }
}
