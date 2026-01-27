package com.example.musicui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    val categories = listOf("Hits", "Happy", "Workout", "Running", "TGIF", "Yoga")
    val grouped = listOf("New Release", "Favorites", "Top Rated").groupBy { it[0] }
    LazyColumn {
        items(grouped.toList()) { (_, list) ->
            this@LazyColumn.stickyHeader {
                Text(
                    text = list.firstOrNull().orEmpty().ifEmpty { "-" },
                    modifier = Modifier.padding(16.dp)
                )
                LazyRow {
                    items(categories) { category ->
                        BrowserItem(
                            category = category,
                            drawable = R.drawable.apps_24,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BrowserItem(
    category: String,
    drawable: Int,
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(200.dp),
        border = BorderStroke(width = 3.dp, color = Color.DarkGray),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = category)
            Image(
                painter = painterResource(id = drawable),
                contentDescription = category,
            )
        }
    }
}