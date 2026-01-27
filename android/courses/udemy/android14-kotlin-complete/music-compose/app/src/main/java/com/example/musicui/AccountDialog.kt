package com.example.musicui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AccountDialog(
    isDialogOpen: MutableState<Boolean>,
) {
    if (isDialogOpen.value) {
        AlertDialog(
            onDismissRequest = {
                isDialogOpen.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = { isDialogOpen.value = false }
                ) {
                    Text(
                        text = "Confirm"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isDialogOpen.value = false }
                ) {
                    Text(
                        text = "Dismiss"
                    )
                }
            },
            title = {
                Text(text = "Add Account")
            },
            text = {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        label = { Text(text = "Email") },
                        modifier = Modifier.padding(top = 16.dp),
                    )
                    TextField(
                        value = "",
                        onValueChange = {},
                        label = { Text(text = "Password") },
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
        )
    }
}