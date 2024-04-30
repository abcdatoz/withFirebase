package abcdatoz.code.withfirebase.ui.screens.auth

import abcdatoz.code.withfirebase.ui.navigation.Routes
import abcdatoz.code.withfirebase.ui.navigation.trackScreen
import abcdatoz.code.withfirebase.utils.AnalyticsManager
import abcdatoz.code.withfirebase.utils.AuthManager
import abcdatoz.code.withfirebase.utils.AuthRes
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.checkScrollableContainerConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
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
fun SignUpScreen(analytics: AnalyticsManager, auth:  AuthManager, navigation: NavController, modifier: Modifier = Modifier) {

    analytics.logScreenView(screenName = Routes.SignUp.route)

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear cuenta", style = TextStyle(fontSize = 40.sp, color = Purple40))
        Spacer(modifier.height(50.dp))

        TextField(
            label = { Text("Correo Electronico") },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it }
        )
        Spacer(modifier.height(20.dp))

        TextField(
            label = { Text("Password") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it }
        )
        Spacer(modifier.height(30.dp))

        Box(modifier.padding(horizontal = 40.dp)) {
            Button(
                onClick = {
                          scope.launch {
                              signUp(email, password,auth, analytics, context, navigation)
                          }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Registrarse")
            }

        }

        Spacer(modifier.height(40.dp))
        ClickableText(
            text = AnnotatedString("ya tienes cuenta? inicia session"),
            onClick = { navigation.popBackStack() }, style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )

    }
}

private suspend fun signUp(
    email: String,
    password: String,
    auth: AuthManager,
    analytics: AnalyticsManager,
    context: Context,
    navigation: NavController
) {
    

    if (email.isNotEmpty() && password.isNotEmpty()){
        when (val result = auth.createUserWithEmailAndPassword(email, password)){
            is AuthRes.Error -> {
                analytics.logButtonClicked(FirebaseAnalytics.Event.SIGN_UP)
                Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show()
                navigation.popBackStack()
            }
            is AuthRes.Success -> {
                analytics.logButtonClicked("Error SignUp: ")
                Toast.makeText(context,"error sign up",Toast.LENGTH_SHORT).show()
            }
        }
    }else{
        Toast.makeText(context, "Capture todos los datos", Toast.LENGTH_SHORT).show()
    }

}


@Composable
@Preview(showBackground = true)
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val analytics = AnalyticsManager(context)
    val auth = AuthManager(context)
    SignUpScreen(analytics,auth, navController)

}