package com.example.network_communication_exercise3.model

data class Response(

    val cases: Cases,
    val continent: String,
    val country: String,
    val day: String,
    val death: Death,
    val population: Int,
    val test: Tests,
    val time: String
)

