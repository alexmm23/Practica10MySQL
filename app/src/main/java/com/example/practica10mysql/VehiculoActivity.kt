package com.example.practica10mysql

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.practica10mysql.Models.Vehicle
import com.example.practica10mysql.Network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException

class VehiculoActivity : AppCompatActivity() {

    private lateinit var marcaEditText: EditText
    private lateinit var modeloEditText: EditText
    private lateinit var anoEditText: EditText
    private lateinit var precioEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var idEditText: EditText
    private lateinit var registrarButton: Button
    private lateinit var actualizarButton: Button
    private lateinit var eliminarButton: Button
    private lateinit var mostrarButton: Button
    private lateinit var buscarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehiculo)

        // Inicializar las vistas
        idEditText = findViewById(R.id.idEditText)
        marcaEditText = findViewById(R.id.marcaEditText)
        modeloEditText = findViewById(R.id.modeloEditText)
        anoEditText = findViewById(R.id.anoEditText)
        precioEditText = findViewById(R.id.precioEditText)
        colorEditText = findViewById(R.id.colorEditText)
        registrarButton = findViewById(R.id.registrarButton)
        actualizarButton = findViewById(R.id.actualizarButton)
        eliminarButton = findViewById(R.id.eliminarButton)
        mostrarButton = findViewById(R.id.mostrarButton)
        buscarButton = findViewById(R.id.buscarButton)
        // Configuración de los botones
        registrarButton.setOnClickListener { registrarVehiculo() }
        actualizarButton.setOnClickListener { actualizarVehiculo() }
        eliminarButton.setOnClickListener { eliminarVehiculo() }
        mostrarButton.setOnClickListener { mostrarVehiculos() }
        buscarButton.setOnClickListener { buscarVehiculo() }
    }

    private fun registrarVehiculo() {
        if (validarVacio(marcaEditText.text.toString()) || validarVacio(modeloEditText.text.toString()) || validarVacio(
                anoEditText.text.toString()
            ) || validarVacio(precioEditText.text.toString()) || validarVacio(colorEditText.text.toString())
        ) {
            showAlertDialog("Registro", "Todos los campos son obligatorios")
            return
        }
        val vehicle = Vehicle(
            id = 0, // ID will be set by the server
            brand = marcaEditText.text.toString(),
            model = modeloEditText.text.toString(),
            year = anoEditText.text.toString().toInt(),
            price = precioEditText.text.toString().toFloat(),
            color = colorEditText.text.toString()
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.createVehicle(vehicle)
                if (response.success) {
                    limpiarCampos()
                }
                showAlertDialog("Registro", response.message)
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    showAlertDialog(
                        "Error al registrar el vehículo",
                        "El vehículo ya existe en la base de datos"
                    )
                }
                Log.e("VehicleCreationError", "Error: ${e.message}")
            } catch (e: Exception) {
                Log.e("VehicleCreationError", "Error: ${e.message}")
            }
        }
    }

    private fun buscarVehiculo() {
        if (validarVacio(idEditText.text.toString())) {
            showAlertDialog("Búsqueda", "Ingresa un ID válido")
            return
        }
        val id = idEditText.text.toString().toInt()
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getVehicles()
                val vehicle = response.find { it.id == id }
                if (vehicle != null) {
                    marcaEditText.setText(vehicle.brand)
                    modeloEditText.setText(vehicle.model)
                    anoEditText.setText(vehicle.year.toString())
                    precioEditText.setText(vehicle.price.toString())
                    colorEditText.setText(vehicle.color)
                } else {
                    showAlertDialog("Búsqueda", "No se encontró el vehículo con ID $id")
                }
            } catch (e: Exception) {
                Log.e("VehicleSearchError", "Error: ${e.message}")
            }
        }
    }

    private fun actualizarVehiculo() {
        if (validarVacio(marcaEditText.text.toString()) || validarVacio(modeloEditText.text.toString()) || validarVacio(
                anoEditText.text.toString()
            ) || validarVacio(precioEditText.text.toString()) || validarVacio(colorEditText.text.toString())
        ) {
            showAlertDialog("Registro", "Todos los campos son obligatorios")
            return
        }
        val vehicle = Vehicle(
            id = idEditText.text.toString().toInt(),
            brand = marcaEditText.text.toString(),
            model = modeloEditText.text.toString(),
            year = anoEditText.text.toString().toInt(),
            price = precioEditText.text.toString().toFloat(),
            color = colorEditText.text.toString()
        )

        val id = idEditText.text.toString().toInt()
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.editVehicle(id, vehicle)
                Log.d("VehicleUpdate", response.message)
                Log.d("VehicleUpdate", response.success.toString())
                if (response.success) {
                    limpiarCampos()
                }
                showAlertDialog("Actualización", response.message)
            } catch (e: Exception) {
                Log.e("VehicleUpdateError", "Error: ${e.message}")
            }
        }
    }

    private fun eliminarVehiculo() {
        if (validarVacio(idEditText.text.toString())) {
            showAlertDialog("Eliminación", "Ingresa un ID válido")
            return
        }
        val id = idEditText.text.toString().toInt()
        if (id == 0) {
            showAlertDialog("Eliminación", "Ingresa un ID válido")
            return
        }
        showConfirmationDialog("Eliminación", "¿Estás seguro de eliminar el vehículo con ID $id?") {
            deleteVehicle(id)
        }
    }

    private fun deleteVehicle(id: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.deleteVehicle(id)
                Log.d("VehicleDeletion", response.message)
                Log.d("VehicleDeletion", response.success.toString())
                if (response.success) {
                    limpiarCampos()
                }
                showAlertDialog("Eliminación", response.message)
            } catch (e: Exception) {
                Log.e("VehicleDeletionError", "Error: ${e.message}")
            }
        }
    }

    private fun mostrarVehiculos() {
        val intent = Intent(this, ListadoActivity::class.java)
        startActivity(intent)
    }

    private fun limpiarCampos() {
        marcaEditText.text.clear()
        modeloEditText.text.clear()
        anoEditText.text.clear()
        precioEditText.text.clear()
        colorEditText.text.clear()
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this@VehiculoActivity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showConfirmationDialog(title: String, message: String, onAccept: () -> Unit) {
        AlertDialog.Builder(this@VehiculoActivity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Sí") { dialog, _ ->
                dialog.dismiss()
                onAccept()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun validarVacio(campo: String): Boolean {
        return campo.isEmpty()
    }
}