package com.example.practica10mysql.Network

import com.example.practica10mysql.Models.ResponseApi
import com.example.practica10mysql.Models.Vehicle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VehicleApi {
    @GET("index.php")
    suspend fun getVehicles(): List<Vehicle>

    @POST("create/index.php")
    suspend fun createVehicle(@Body vehicle: Vehicle): ResponseApi

    @DELETE("delete/index.php")
    suspend fun deleteVehicle(@Query("id") id: Int): ResponseApi

    @POST("edit/index.php")
    suspend fun editVehicle(@Query("id")id: Int, @Body vehicle: Vehicle): ResponseApi
}


object RetrofitInstance{
    private const val BASE_URL = "http://192.168.0.127/api/cars/"
    val api: VehicleApi by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehicleApi::class.java)
    }
}