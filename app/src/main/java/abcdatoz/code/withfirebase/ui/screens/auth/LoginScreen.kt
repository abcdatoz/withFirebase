package abcdatoz.code.withfirebase.ui.screens.auth

import abcdatoz.code.withfirebase.R
import abcdatoz.code.withfirebase.ui.navigation.Routes
import abcdatoz.code.withfirebase.ui.navigation.trackScreen
import abcdatoz.code.withfirebase.utils.AnalyticsManager
import abcdatoz.code.withfirebase.utils.AuthManager
import abcdatoz.code.withfirebase.utils.AuthRes
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.withfirebase.ui.theme.Purple40
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    analytics: AnalyticsManager,
    auth: AuthManager,
    navigation: NavController,
    modifier: Modifier = Modifier
) {
    analytics.logScreenView(screenName = Routes.Login.route)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()) { result ->
        when(val account = auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
            is AuthRes.Success -> {
                val credential = GoogleAuthProvider.getCredential(account?.data?.idToken, null)
                scope.launch {
                    val fireUser = auth.signInWithGoogleCredential(credential)
                    if (fireUser != null){
                        Toast.makeText(context, "Bienvenidx", Toast.LENGTH_SHORT).show()
                        navigation.navigate(Routes.Home.route){
                            popUpTo(Routes.Login.route){
                                inclusive = true
                            }
                        }
                    }
                }
            }
            is AuthRes.Error -> {
                analytics.logError("Error SignIn: ${account.errorMessage}")
                Toast.makeText(context, "Errrrror: ${account.errorMessage}", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier.fillMaxSize()) {

        ClickableText(
            text = AnnotatedString("No tienes una cuenta?, registrate"),
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(50.dp),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            ),
            onClick = {

                analytics.logButtonClicked("Click: No tienes una cuenta? Regístrate")
                navigation.navigate(Routes.SignUp.route)


            }
        )

    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_firebase),
            contentDescription = "firebase",
            modifier = modifier.size(100.dp)
        )
        Spacer(modifier = modifier.height(10.dp))

        Text("Firebase Android", fontSize = 30.sp)
        Spacer(modifier = modifier.height(30.dp))

        TextField(
            label = { Text("Correo Electronico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            value = email,
            onValueChange = { email = it })

        Spacer(modifier = modifier.height(10.dp))


        TextField(
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = password,
            onValueChange = { password = it }
        )

        Spacer(modifier = modifier.height(20.dp))

        Box(modifier = modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {

            Button(
                onClick = {

                          scope.launch {
                              emailPassSigIn(email,password, auth,analytics, context, navigation)
                          }
                },
                shape = RoundedCornerShape(50.dp),
            ) {
                Text("Iniciar Sesión")
            }

        }

        Spacer(modifier = modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("?forgot pass?"),
            onClick = {
                analytics.logButtonClicked("Click: Olvidaste tu password?")
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
        Spacer(modifier = modifier.height(25.dp))
        Text("-------------- o -----------------")
        Spacer(modifier = modifier.height(25.dp))

        SocialMediaButton(
            onClick = {
                scope.launch {
                    incognitoSignIn(auth, analytics, context, navigation)
                }
            },
            text = "Continuar como invitado",
            icon = R.drawable.ic_incognito,
            color = Color(0xFF6D6B6B)
        )
        Spacer(modifier.height(15.dp))
        SocialMediaButton(
            onClick = {
                scope.launch {
                    auth.signInWithGoogle(googleSignInLauncher)
                }
            },
            text = "Continuar con Google",
            icon = R.drawable.ic_google,
            color = Color(0xFFF1F1F1)
        )

        //  Spacer(modifier.height(25.dp))
        //ClickableText(text = AnnotatedString("Forzar Cierre Crashlytics"), onClick = {})

    }

}

private suspend fun emailPassSigIn(
    email: String,
    password: String,
    auth: AuthManager,
    analytics: AnalyticsManager,
    context: Context,
    navigation: NavController
) {





    if (email.isNotEmpty() && password.isNotEmpty()){
        when (val result = auth.signInWithEmailAndPassword (email, password)){
            is AuthRes.Success -> {
                analytics.logButtonClicked("Click: iniciar sesssion con correo y password")
                navigation.navigate(Routes.Home.route) {
                    popUpTo(Routes.Login.route) {
                        inclusive = true
                    }
                }
                Toast.makeText(context, "sigin Exitoso", Toast.LENGTH_SHORT).show()

            }
            is AuthRes.Error -> {
                analytics.logButtonClicked("Error SignUp: ")
                Toast.makeText(context,"error sign up", Toast.LENGTH_SHORT).show()
            }
        }
    }else{
        Toast.makeText(context, "Capture todos los datos", Toast.LENGTH_SHORT).show()
    }

}

private suspend fun incognitoSignIn(
    auth: AuthManager,
    analytics: AnalyticsManager,
    context: Context,
    navigation: NavController
) {

    when (val result = auth.signInAnonymously()) {
        is AuthRes.Success -> {
            analytics.logButtonClicked("Click: continuar como invitado")
            navigation.navigate(Routes.Home.route) {
                popUpTo(Routes.Login.route) {
                    inclusive = true
                }
            }
        }

        is AuthRes.Error -> {
            analytics.logError("Error SigIn Incognito ${result.errorMessage}")
        }
    }
}


@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    val contexto = LocalContext.current
    val analytics = AnalyticsManager(contexto)
    val auth = AuthManager(contexto)
    val navController = rememberNavController()

    LoginScreen(analytics = analytics, auth, navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialMediaButton(
    onClick: () -> Unit,
    text: String,
    icon: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    var click by remember { mutableStateOf(false) }

    Surface(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 40.dp)
            .clickable { click = !click },
        shape = RoundedCornerShape(50),
        border = BorderStroke(
            width = 1.dp,
            color = if (icon == R.drawable.ic_launcher_background) color else Color.Gray
        ),
        color = color
    ) {
        Row(
            modifier = modifier.padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = modifier.size(24.dp),
                contentDescription = text,
                tint = Color.Unspecified
            )
            Spacer(modifier.width(8.dp))
            Text(
                "$text",
                color = if (icon == R.drawable.ic_launcher_background) Color.White else Color.Black
            )
            click = true
        }
    }

}