package com.example.location

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.location.ui.theme.LocationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: LocationViewModel = viewModel()
            LocationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LocationApp(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LocationApp(
    viewModel: LocationViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationScreen(
        viewModel = viewModel,
        context = context,
        locationUtils = locationUtils,
        modifier = modifier,
    )
}

@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
    context: Context,
    locationUtils: LocationUtils,
    modifier: Modifier = Modifier,
) {
    val location = viewModel.location.value
    val address = location?.let { locationUtils.reverseGeocodeLocation(it) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                // User has access to location
                locationUtils.requestLocationUpdates(viewModel)
            } else {
                // Ask for permission
                if (locationUtils.shouldShowLocationRationale()) {
                    Toast.makeText(
                        context,
                        "Location Permission is required for the feature to work",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Location Permission is required. Please enable it in the Android Settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        },
    )
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (location != null) {
            Text(text = "Address: ${location.latitude} ${location.longitude} \n $address")
        } else {
            Text(text = "Location not available")
        }
        Button(
            onClick = {
                if (locationUtils.hasLocationPermission()) {
                    // Permission already granted, update the location
                    locationUtils.requestLocationUpdates(viewModel)
                } else {
                    // Request location permission
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
        ) {
            Text(text = "Get Location")
        }
    }
}