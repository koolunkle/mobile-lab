package com.example.recipe

sealed class Screen(val route: String) {
    data object RecipeScreen : Screen("recipeScreen")
    data object DetailScreen : Screen("detailScreen")
}