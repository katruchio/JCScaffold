package com.example.jcscaffold

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.AppTheme
import com.example.jcscaffold.ui.theme.screens.AddItemScreen
import com.example.jcscaffold.ui.theme.screens.ItemsScreen
import com.example.jcscaffold.ui.theme.screens.ProfileScreen
import com.example.jcscaffold.ui.theme.screens.SettingsScreen
import com.example.navigation.NavigationHost
import com.example.navigation.rememberNavigation


val RootTabs : List<AppRoute.Tab> = listOf(AppRoute.Tab.Items, AppRoute.Tab.Settings, AppRoute.Tab.Profile)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                AppScaffold()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(itemsRepository: ItemsRepository = ItemsRepository.get()){

    val navigation = rememberNavigation(initialRoute = AppRoute.Tab.Items)
    val (router, navigationState) = navigation

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource((navigationState.currentRoute as? AppRoute)?.titleRes ?: R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!navigationState.isRoot) router.pop()
                        }
                    ) {
                        Icon(
                            imageVector = if(navigationState.isRoot)
                            Icons.Default.Menu
                            else
                            Icons.Default.ArrowBack,
                            contentDescription = "Main menu"
                        )
                    }
                },
                actions = {
                    var showPopupMenu by remember { mutableStateOf(false)}
                    val context = LocalContext.current
                    IconButton(
                        onClick = {showPopupMenu = true}
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More actions"
                        )
                    }
                    DropdownMenu(
                        expanded = showPopupMenu,
                        onDismissRequest = {showPopupMenu = false}
                    ) {
                        DropdownMenuItem(
                            text = { Text("About")},
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Scaffold App",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showPopupMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Clear")},
                            onClick = {
                                itemsRepository.clear()
                                showPopupMenu = false
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if(navigationState.currentRoute == AppRoute.Tab.Items) {
                FloatingActionButton(
                    onClick = {router.launch(AppRoute.AddItem)}
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new item"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if(navigationState.isRoot) {
                NavigationBar{
                    RootTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = navigationState.currentRoute == tab,
                            label = { Text(stringResource(tab.titleRes))},
                            onClick = {
                                router.restart(tab)
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = stringResource(tab.titleRes)
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavigationHost(
            navigation = navigation,
            modifier = Modifier.padding(paddingValues)
        ) {currentRoute ->
            when(currentRoute) {
                AppRoute.Tab.Items -> ItemsScreen()
                AppRoute.Tab.Settings -> SettingsScreen()
                AppRoute.Tab.Profile -> ProfileScreen()
                AppRoute.AddItem -> AddItemScreen()
            }
        }

    }
}




@Composable
@Preview(showSystemUi = true)
fun AppScaffoldPreview() {
    AppTheme(false){
        AppScaffold()
    }
}

//enum class Tab(
//    val label : String,
//    val icon : ImageVector
//){
//    Items("Items",Icons.Default.List),
//    Settings("Settings", Icons.Default.Settings),
//    Profile("Profile", Icons.Default.AccountBox )
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = false)
//@Composable
//fun AppScreen(){
//    var currentTab by remember {
//        mutableStateOf(Tab.Items)
//    }
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(
//                        text = currentTab.label,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary
//                ),
//                navigationIcon = {
//                    IconButton(
//                        onClick = {/*todo*/}
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Menu,
//                            contentDescription = "Main Menu"
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(
//                        onClick = {/* todo */}
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Settings,
//                            contentDescription = "Settings"
//                        )
//                    }
//                    IconButton(
//                        onClick = {/* todo */}
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "More variants"
//                        )
//                    }
//                }
//            )
//        },
//
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {/* todo */}
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Add new item"
//                )
//            }
//        },
//
//        floatingActionButtonPosition = FabPosition.End,
//
//        bottomBar = {
//            NavigationBar {
//                Tab.values().forEach { tab ->
//                    NavigationBarItem(
//                        selected = tab == currentTab,
//                        onClick = { currentTab = tab},
//                        label = { Text(tab.label)},
//                        icon ={
//                            Icon(
//                                imageVector = tab.icon,
//                                contentDescription = "Items List"
//                            )
//                        }
//                    )
//                }
//            }
//        },
//    ) {paddingValues ->
//        Surface(
//            color = MaterialTheme.colorScheme.background,
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxWidth()
//        ) {
//            TabScreen(tab = currentTab)
//        }
//
//    }
//}
//
//@Composable
//@Preview(showSystemUi = true)
//fun AppScreenPreview() {
//    AppTheme(darkTheme = false) {
//        AppScreen()
//    }
//}
//
//@Composable
//fun ItemsScreen(){
//    Box(
//        contentAlignment = Alignment.Center
//    ){
//        Text(text = "Items Screen", fontSize = 28.sp)
//    }
//}
//@Composable
//fun SettingsScreen(){
//    Box(
//        contentAlignment = Alignment.Center
//    ){
//        Text(text = "Settings Screen", fontSize = 28.sp)
//    }
//}
//@Composable
//fun ProfileScreen(){
//    Box(
//        contentAlignment = Alignment.Center
//    ){
//        Text(text = "Items Screen", fontSize = 28.sp)
//    }
//}
//
//@Composable
//fun TabScreen(tab : Tab){
//    when(tab){
//        Tab.Items -> ItemsScreen()
//        Tab.Settings -> SettingsScreen()
//        Tab.Profile -> ProfileScreen()
//    }
//}