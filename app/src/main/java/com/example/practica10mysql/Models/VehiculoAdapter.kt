package com.example.practica10mysql
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practica10mysql.Models.Vehicle
import com.example.practica10mysql.R

class VehiculoAdapter(private val vehiculos: List<Vehicle>) :
    RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder>() {

    class VehiculoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val marcaTextView: TextView = itemView.findViewById(R.id.marcaTextView)
        val modeloTextView: TextView = itemView.findViewById(R.id.modeloTextView)
        val anioTextView: TextView = itemView.findViewById(R.id.anioTextView)
        val colorTextView: TextView = itemView.findViewById(R.id.colorTextView)
        val precioTextView: TextView = itemView.findViewById(R.id.precioTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiculoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehiculo, parent, false)
        return VehiculoViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VehiculoViewHolder, position: Int) {
        val vehiculo = vehiculos[position]
        holder.marcaTextView.text = "Id:${vehiculo.id}\nMarca: ${vehiculo.brand}"
        holder.modeloTextView.text = "Modelo: ${vehiculo.model}"
        holder.anioTextView.text = "Año: ${vehiculo.year}"
        holder.colorTextView.text = "Color: ${vehiculo.color}"
        holder.precioTextView.text = "Precio: $${vehiculo.price}"
    }

    override fun getItemCount(): Int = vehiculos.size
}
