package com.example.network_communication_exercise3

import com.example.network_communication_exercise3.model.CovidData
import retrofit2.http.GET

interface CovidApiService {
    @GET("/countries?search=italy")
    suspend fun getCovidDetails(): CovidData

}