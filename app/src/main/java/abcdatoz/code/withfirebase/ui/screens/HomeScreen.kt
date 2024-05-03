package abcdatoz.code.withfirebase.ui.screens

import abcdatoz.code.withfirebase.R
import abcdatoz.code.withfirebase.ui.navigation.Routes
import abcdatoz.code.withfirebase.ui.navigation.trackScreen
import abcdatoz.code.withfirebase.ui.screens.db.ContactsScreen
import abcdatoz.code.withfirebase.ui.screens.db.NotesScreen
import abcdatoz.code.withfirebase.ui.screens.storage.CloudStorageScreen
import abcdatoz.code.withfirebase.utils.AnalyticsManager
import abcdatoz.code.withfirebase.utils.AuthManager
import abcdatoz.code.withfirebase.utils.FirestoreManager
import abcdatoz.code.withfirebase.utils.RealtimeManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.analytics.FirebaseAnalytics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    analytics: AnalyticsManager,
    auth: AuthManager,
    navigation: NavController,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    var showDialog by remember { mutableStateOf(false) }

    val user = auth.getCurrentUser()

    val context = LocalContext.current

    val onLogoutConfirmed: () -> Unit = {

        auth.signOut()
        navigation.navigate(Routes.Login.route) {
            popUpTo(Routes.Home.route) {
                inclusive = true
            }
        }
    }
    analytics.logScreenView(screenName = Routes.Home.route)

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    if (user?.photoUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user?.photoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Imagen",
                            placeholder = painterResource(id = R.drawable.profile),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(40.dp))


                    } else {

                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "nothing",
                            modifier = modifier
                                .padding(end = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                        )

                    }


                    Spacer(modifier.width(10.dp))

                    Column {

                        Text(
                            text = if (!user?.displayName.isNullOrEmpty()) "Hola ${user?.displayName}" else "Bienvenido",
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = if (!user?.email.isNullOrEmpty()) " ${user?.email}" else "Anonimo",
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }


                }
            },
                colors = TopAppBarDefaults.smallTopAppBarColors(),
                actions = {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = modifier.alpha(1f)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Forza Rey")
                    }

                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar session")
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { contentPadding ->
        Box(modifier.padding(contentPadding)) {

            if (showDialog) {
                LogoutDialog(onConfirmLogout = {
                    onLogoutConfirmed()
                    showDialog = false
                }, onDismiss = { showDialog = false })
            }

            BottomNavGraph(navController = navController, context = context, auth =auth)

        }
    }

}


@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    val context = LocalContext.current
    val analytics = AnalyticsManager(context)
    val auth = AuthManager(context)
    val navController = rememberNavController()
    HomeScreen(analytics, auth, navController)
}


@Composable
fun LogoutDialog(onConfirmLogout: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            Button(
                onClick = onConfirmLogout
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun BottomBar(navController: NavHostController) {

    val screens = listOf(
        BottomNavScreen.Contact,
        BottomNavScreen.Note,
        BottomNavScreen.Photos
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            if (currentDestination != null) {
                AddItem(
                    screens = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screens: BottomNavScreen,
    currentDestination: NavDestination,
    navController: NavHostController
) {
    NavigationBarItem(
        label = { Text(text = screens.title) },
        icon = { Icon(imageVector = screens.icon, contentDescription = "Icons") },
        selected = currentDestination.hierarchy?.any {
            it.route == screens.route
        } == true,
        onClick = {
            navController.navigate(screens.route) {
                popUpTo(navController.graph.id)
                launchSingleTop = true
            }
        }
    )
}

@Composable
fun BottomNavGraph(navController: NavHostController, context: Context, auth: AuthManager) {
    val realtime = RealtimeManager(context )
    val firestore = FirestoreManager(context)

    NavHost(navController = navController, startDestination = BottomNavScreen.Contact.route) {
        composable(route = BottomNavScreen.Contact.route) {
            ContactsScreen(realtime = realtime, auth)
        }

        composable(route = BottomNavScreen.Note.route) {
            NotesScreen(firestore = firestore )
        }

        composable(route = BottomNavScreen.Photos.route) {
            CloudStorageScreen()
        }
    }
}


sealed class BottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    object Contact :
        BottomNavScreen(route = "contact", title = "contactos", icon = Icons.Default.Person)

    object Note : BottomNavScreen(route = "notes", title = "notas", icon = Icons.Default.List)
    object Photos : BottomNavScreen(route = "photos", title = "photos", icon = Icons.Default.Face)

}