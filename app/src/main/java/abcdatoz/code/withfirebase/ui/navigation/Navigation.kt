package abcdatoz.code.withfirebase.ui.navigation

import abcdatoz.code.withfirebase.ui.screens.HomeScreen
import abcdatoz.code.withfirebase.ui.screens.Screen
import abcdatoz.code.withfirebase.ui.screens.auth.LoginScreen
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics


@Composable
fun Navigation(
    analytics: FirebaseAnalytics,
    context: Context,
    navController: NavHostController = rememberNavController()
) {


    Screen {
        NavHost(navController = navController, startDestination = Routes.Login.route) {

            composable(Routes.Login.route) {
                LoginScreen(
                    analytics,
                    navigateToHome = { navController.navigate(Routes.Home.route) }
                )
            }

            composable(Routes.Home.route) {
                HomeScreen(analytics)
            }
        }
    }

}