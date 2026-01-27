package com.example.shoppinglist

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val _location: MutableState<LocationData?> = mutableStateOf(null)
    val location: State<LocationData?> = _location

    private val _address: MutableState<List<GeocodingResult>> = mutableStateOf(listOf())
    val address: State<List<GeocodingResult>> = _address

    fun updateLocation(newLocation: LocationData) {
        _location.value = newLocation
    }

    fun fetchAddress(latlng: String) {
        try {
            viewModelScope.launch {
                val result = RetrofitClient.create().getAddressFromCoordinates(
                    latlng = latlng,
                    apiKey = "",
                )
                _address.value = result.results
            }
        } catch (e: Exception) {
            Log.d("fetchAddress", "${e.cause} ${e.message}")
        }
    }
}