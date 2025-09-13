package co.uk.lmu.caloriesapp.registration.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.uk.lmu.caloriesapp.registration.RegistrationViewModel

@Composable
fun HeightScreen(navController: NavController, viewModel: RegistrationViewModel) {
    val height by viewModel.height.collectAsState()
    var input by remember { mutableStateOf(height?.toString() ?: "") }
    val heightInt = input.toIntOrNull()
    val scrollState = rememberScrollState()
    // layout for height input screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Your Height")
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = input,
            onValueChange = {
                if (it.length <= 3) input = it
                heightInt?.let { value ->
                    if (value in 100..250) viewModel.setHeight(value)
                }
            },
            label = { Text("Height") }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
                ) { Text("Back") }
            Button(onClick = {
                if (heightInt != null && heightInt in 100..250) {
                    viewModel.setHeight(heightInt)
                    navController.navigate("activity_level")
                }
            },colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            ), enabled = heightInt in 100..250) {
                Text("Next")
            }
        }
    }
}
