package com.example.musicui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // Allow use to find out on which "Screen" we current are
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentScreen = remember { viewModel.currentScreen.value }
    // change that to current screen's title
    val title = remember { mutableStateOf(currentScreen.title) }

    val isDialogOpen = remember { mutableStateOf(false) }
    val isSheetFullScreen = remember { mutableStateOf(false) }

    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            NavigationBar(modifier = Modifier.wrapContentSize()) {
                screensInBottom.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.bottomRoute,
                        onClick = {
                            navController.navigate(route = item.bottomRoute)
                            title.value = item.bottomTitle
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.bottomTitle,
                            )
                        },
                        label = { Text(text = item.bottomTitle) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Green,
                            selectedTextColor = Color.Green,
                            unselectedIconColor = Color.Black,
                            unselectedTextColor = Color.Black,
                        ),
                    )
                }
            }
        }
    }
    if (isSheetFullScreen.value) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            onDismissRequest = { isSheetFullScreen.value = false },
            modifier = if (isSheetFullScreen.value) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
        )
    }
    ModalNavigationDrawer(
        gesturesEnabled = !drawerState.isClosed,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .fillMaxHeight(),
            ) {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(screensInDrawer) { item ->
                        DrawerItem(
                            selected = currentRoute == item.drawerRoute,
                            item = item,
                            onDrawerItemClicked = {
                                scope.launch { drawerState.close() }
                                if (item.drawerRoute == Screen.DrawerScreen.AddAccount.drawerRoute) {
                                    // Open dialog
                                    isDialogOpen.value = true
                                } else {
                                    navController.navigate(route = item.drawerRoute)
                                    title.value = item.drawerTitle
                                }
                            },
                        )
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = title.value) },
                    navigationIcon = {
                    IconButton(
                        onClick = {
                            // Open the drawer
                            scope.launch { drawerState.open() }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Menu"
                        )
                    }
                    },
                    actions = {
                        IconButton(onClick = { isSheetFullScreen.value = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                            )
                        }
                    },
                )
            },
            bottomBar = bottomBar,
        ) { paddingValues ->
            Navigation(
                viewModel = viewModel,
                navController = navController,
                paddingValues = paddingValues,
            )
            AccountDialog(isDialogOpen = isDialogOpen)
        }
    }
}

@Composable
fun DrawerItem(
    selected: Boolean,
    item: Screen.DrawerScreen,
    onDrawerItemClicked: () -> Unit,
) {
    val color = if (selected) Color.Green else Color.White
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(color = color)
            .clickable { onDrawerItemClicked() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.drawerTitle,
            modifier = Modifier.padding(top = 4.dp, end = 8.dp)
        )
        Text(
            text = item.drawerTitle,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetLayout(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = Color.White,
        dragHandle = null,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(300.dp)
                .background(color = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.settings_24),
                        contentDescription = "Setting",
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        color = Color.Black,
                    )
                }
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.share_24),
                        contentDescription = "Share",
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text(
                        text = "Share",
                        fontSize = 20.sp,
                        color = Color.Black,
                    )
                }
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.help_24),
                        contentDescription = "Help",
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text(
                        text = "Help",
                        fontSize = 20.sp,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(
    viewModel: MainViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.DrawerScreen.Account.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        // Bottom Screens
        composable(Screen.BottomScreen.Home.route) {
            HomeScreen()
        }
        composable(Screen.BottomScreen.Library.route) {
            LibraryScreen()
        }
        composable(Screen.BottomScreen.Browse.route) {
            BrowseScreen()
        }
        // Drawer Screens
        composable(Screen.DrawerScreen.Account.route) {
            AccountScreen()
        }
        composable(Screen.DrawerScreen.Subscription.route) {
            SubscriptionScreen()
        }
    }
}