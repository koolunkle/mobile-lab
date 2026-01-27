package com.example.chatroom

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatroom.screen.ChatRoomListScreen
import com.example.chatroom.screen.ChatScreen
import com.example.chatroom.screen.LoginScreen
import com.example.chatroom.screen.Screen
import com.example.chatroom.screen.SignUpScreen
import com.example.chatroom.ui.theme.ChatroomTheme
import com.example.chatroom.viewmodel.AuthViewModel
import com.example.chatroom.viewmodel.MessageViewModel
import com.example.chatroom.viewmodel.RoomViewModel

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val roomViewModel: RoomViewModel = viewModel()
            val messageViewModel: MessageViewModel = viewModel()
            val navController = rememberNavController()
            ChatroomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationGraph(
                        authViewModel = authViewModel,
                        roomViewModel = roomViewModel,
                        messageViewModel = messageViewModel,
                        navController = navController,
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    authViewModel: AuthViewModel, roomViewModel: RoomViewModel, messageViewModel: MessageViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController, startDestination = Screen.SignupScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) },
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onSignInSuccess = { navController.navigate(Screen.ChatRoomsScreen.route) },
            )
        }
        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomListScreen(
                roomViewModel = roomViewModel,
                onJoinClicked = { room ->
                    navController.navigate("${Screen.ChatScreen.route}/${room.id}")
                },
            )
        }
        composable("${Screen.ChatScreen.route}/{roomId}") {
            val roomId: String = it.arguments?.getString("roomId") ?: ""
            ChatScreen(
                roomId = roomId,
                messageViewModel = messageViewModel,
            )
        }
    }
}