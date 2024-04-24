package abcdatoz.code.withfirebase.ui.screens

import abcdatoz.code.withfirebase.ui.theme.WithFirebaseTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier



@Composable
fun Screen (content: @Composable () -> Unit){
    WithFirebaseTheme{
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            content()
        }
    }
}