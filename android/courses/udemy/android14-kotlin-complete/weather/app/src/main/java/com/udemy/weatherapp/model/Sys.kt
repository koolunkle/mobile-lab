package com.udemy.weatherapp.model

import java.io.Serializable

data class Sys(
    val type: Int, val message: Double, val country: String, val sunrise: Long, val sunset: Long
) : Serializable