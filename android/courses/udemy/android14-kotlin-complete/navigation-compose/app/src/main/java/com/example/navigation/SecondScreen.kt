package com.example.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigation.ui.theme.NavigationTheme

@Composable
fun SecondScreen(
    name: String,
    navigateToFirstScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "This is the Second Screen",
            fontSize = 24.sp,
        )
        Text(
            text = "Welcome $name",
            fontSize = 24.sp,
        )
        Button(onClick = { navigateToFirstScreen(name) }) {
            Text(text = "Go to First Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecondPreview() {
    NavigationTheme {
        SecondScreen(
            name = "Hello, World!",
            navigateToFirstScreen = {}
        )
    }
}
