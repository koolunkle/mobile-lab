package com.example.wishlist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.wishlist.data.Wish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: WishViewModel,
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            AppBarScreen(
                title = "WishList",
                onBackNavClicked = {
                    Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show()
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(route = Screen.AddScreen.route + "/0L") },
                contentColor = Color.White,
                containerColor = Color.Black,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        }
    ) { paddingValues ->
        val wishList by viewModel.getAllWishes.collectAsStateWithLifecycle(initialValue = listOf())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.White),
        ) {
            items(
                items = wishList,
                key = { it.id },
            ) { wish ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        when (it) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                viewModel.deleteWish(wish)
                            }

                            SwipeToDismissBoxValue.EndToStart -> {
                                return@rememberSwipeToDismissBoxState false
                            }

                            SwipeToDismissBoxValue.Settled -> {
                                return@rememberSwipeToDismissBoxState false
                            }
                        }
                        return@rememberSwipeToDismissBoxState true
                    },
                    positionalThreshold = { it * 0.7f }
                )
                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromEndToStart = false,
                    backgroundContent = {
                        val color = when (dismissState.dismissDirection) {
                            SwipeToDismissBoxValue.StartToEnd -> Color.Red
                            SwipeToDismissBoxValue.EndToStart -> Color.Transparent
                            SwipeToDismissBoxValue.Settled -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Icon",
                                tint = Color.White,
                            )
                        }
                    }
                ) {
                    WishItem(
                        wish = wish,
                        onClick = {
                            val id = wish.id
                            navController.navigate(route = Screen.AddScreen.route + "/$id")
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun WishItem(
    wish: Wish,
    onClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.DarkGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = wish.title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = wish.description,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}