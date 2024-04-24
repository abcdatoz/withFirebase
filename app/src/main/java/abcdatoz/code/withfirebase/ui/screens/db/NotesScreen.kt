package abcdatoz.code.withfirebase.ui.screens.db

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun NotesScreen(modifier: Modifier = Modifier){

    Column {
        Text("Otro modulo construyendose")
    }
}

@Composable
@Preview(showBackground = true)
fun NotesScreenPreview()
{
    NotesScreen()
}