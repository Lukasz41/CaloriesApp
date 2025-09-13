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
fun WeightScreen(navController: NavController, viewModel: RegistrationViewModel) {
    val weight by viewModel.weight.collectAsState()
    var input by remember { mutableStateOf(weight?.toString() ?: "") }
    val weightInt = input.toIntOrNull()
    val scrollState = rememberScrollState()
    // layout for entering current weight
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // screen title
        Text("Enter Current Weight")
        Spacer(modifier = Modifier.height(8.dp))
        // weight input field
        TextField(
            value = input,
            onValueChange = {
                if (it.length <= 3) input = it
                weightInt?.let { value ->
                    if (value in 30..300) viewModel.setWeight(value)
                }
            },
            label = { Text("Weight") }
        )

        Spacer(modifier = Modifier.height(24.dp))
        // navigation buttons
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
                ) { Text("Back") }
            Button(onClick = {
                if (weightInt != null && weightInt in 30..300) {
                    viewModel.setWeight(weightInt)
                    navController.navigate("height")
                }
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                ),
                enabled = weightInt in 30..300) {
                Text("Next")
            }
        }
    }
}
