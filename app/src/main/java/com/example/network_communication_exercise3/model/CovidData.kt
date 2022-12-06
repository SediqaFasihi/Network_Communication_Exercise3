package com.example.network_communication_exercise3.model




data class CovidData(
    val error: List<Any>,
    val get: String,
    val parameter: Parameter,
    val result: Int,
    val response: List<Response>
)
