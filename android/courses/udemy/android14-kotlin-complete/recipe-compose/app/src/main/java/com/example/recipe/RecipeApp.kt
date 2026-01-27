package com.example.recipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun RecipeApp(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: MainViewModel = viewModel()
    val viewState by viewModel.categoriesState
    NavHost(
        navController = navController,
        startDestination = Screen.RecipeScreen.route,
    ) {
        composable(route = Screen.RecipeScreen.route) {
            RecipeScreen(
                viewState = viewState,
                navigateToDetail = {
                    // This part is responsible for passing data from the current screen to the detail screen.
                    // It utilizes the savedStateHandle, which is a component of the Compose navigation framework.
                    navController.currentBackStackEntry?.savedStateHandle?.set("category", it)
                    navController.navigate(Screen.DetailScreen.route)
                },
                modifier = modifier,
            )
        }
        composable(route = Screen.DetailScreen.route) {
            val category =
                navController.previousBackStackEntry?.savedStateHandle?.get<Category>("category")
                    ?: Category("", "", "", "")
            CategoryDetailScreen(
                category = category,
                modifier = modifier,
            )
        }
    }
}