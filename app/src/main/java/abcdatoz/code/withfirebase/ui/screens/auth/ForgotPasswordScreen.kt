package abcdatoz.code.withfirebase.ui.screens.auth

import abcdatoz.code.withfirebase.ui.navigation.Routes
import abcdatoz.code.withfirebase.ui.navigation.trackScreen
import abcdatoz.code.withfirebase.utils.AnalyticsManager
import abcdatoz.code.withfirebase.utils.AuthManager
import abcdatoz.code.withfirebase.utils.AuthRes
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.withfirebase.ui.theme.Purple40
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(analytics: AnalyticsManager,auth: AuthManager,  navigation: NavController, modifier: Modifier = Modifier) {

    analytics.logScreenView(screenName = Routes.ForgotPassword.route)

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Olvido su password", style = TextStyle(fontSize = 40.sp, color = Purple40))
        Spacer(modifier.height(50.dp))

        TextField(
            label = { Text("Correo Electronico") },
            value = email,
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier.height(30.dp))

        Box(modifier.padding(horizontal = 40.dp)) {
            Button(
                onClick = {
                          
                          scope.launch {
                              when (val res = auth.resetPassword(email)){
                                  is AuthRes.Success -> {

                                      analytics.logButtonClicked("Reset password ${email}")
                                      Toast.makeText( context,"Correo enviado",Toast.LENGTH_SHORT).show()
                                      navigation.navigate(Routes.Login.route)
                                  }
                                  is AuthRes.Error -> {

                                      analytics.logError("Reset password error ${email} : ${res.errorMessage}")
                                      Toast.makeText(context, "Error al enviar correo", Toast.LENGTH_SHORT).show()
                                  }
                              }
                          }


                          },
                shape = RoundedCornerShape(50.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Recuperar password")
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun ForgotPasswordScreenPreview() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val analytics = AnalyticsManager(context)
    val auth = AuthManager(context)

    ForgotPasswordScreen(analytics,auth,navController)
}
