package abcdatoz.code.withfirebase.ui.navigation

import abcdatoz.code.withfirebase.ui.screens.HomeScreen
import abcdatoz.code.withfirebase.ui.screens.Screen
import abcdatoz.code.withfirebase.ui.screens.auth.ForgotPasswordScreen
import abcdatoz.code.withfirebase.ui.screens.auth.LoginScreen
import abcdatoz.code.withfirebase.ui.screens.auth.SignUpScreen
import abcdatoz.code.withfirebase.utils.AnalyticsManager
import abcdatoz.code.withfirebase.utils.AuthManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.FirebaseUser


@Composable
fun Navigation(
    context: Context,
    navController: NavHostController = rememberNavController()
) {

    var analytics: AnalyticsManager = AnalyticsManager(context)
    val authManager: AuthManager = AuthManager(context)

    val user: FirebaseUser? = authManager.getCurrentUser()
    Screen {
        NavHost(
            navController = navController,
            startDestination = if (user == null) Routes.Login.route else Routes.Home.route
        ) {

            composable(Routes.Login.route) {
                LoginScreen(
                    analytics,
                    authManager,
                    navController
                )
            }

            composable(Routes.Home.route) {
                HomeScreen(
                    analytics,
                    authManager,
                    navController
                )
            }

            composable(Routes.ForgotPassword.route) {
                ForgotPasswordScreen(
                    analytics,
                    authManager,
                    navController
                )
            }

            composable((Routes.SignUp.route)) {
                SignUpScreen(
                    analytics,
                    authManager,
                    navController
                )
            }


        }
    }

}

@Composable
fun trackScreen(name: String, analytics: FirebaseAnalytics) {
    DisposableEffect(Unit) {
        onDispose {
            analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, name)
            }
        }
    }
}