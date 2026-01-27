package com.example.musicui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun LibraryScreen() {
    LazyColumn {
        items(libraries) { library ->
            LibraryItem(library = library)
        }
    }
}

@Composable
fun LibraryItem(library: Library) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        ) {
            Row {
                Icon(
                    painter = painterResource(id = library.icon),
                    contentDescription = library.name,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                Text(text = library.name)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Arrow Right"
            )
        }
        Divider(color = Color.LightGray)
    }
}