package com.example.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navigation.ui.theme.NavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "firstScreen"
    ) {
        composable(route = "firstScreen") {
            FirstScreen(
                navigationToSecondScreen = { name ->
                    navController.navigate("secondScreen/$name")
                },
                modifier = modifier,
            )
        }
        composable(route = "secondScreen/{name}") {
            val name = it.arguments?.getString("name").orEmpty()
            SecondScreen(
                name = name,
                navigateToFirstScreen = {
                    navController.navigate("firstScreen")
                },
                modifier = modifier,
            )
        }
    }
}