package co.uk.lmu.caloriesapp.registration.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.uk.lmu.caloriesapp.registration.RegistrationViewModel

@Composable
fun WeightLossRateScreen(navController: NavController, viewModel: RegistrationViewModel) {
    val selectedRate by viewModel.weightLossRate.collectAsState()
    // layout for selecting weekly weight loss rate
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose Weekly Weight Loss Rate (kg/week)")
        // list of rate options as radio buttons
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(0.25f, 0.5f, 0.75f, 1f).forEach { rate ->
                WeightLossRateOption(rate, selectedRate, viewModel::setWeightLossRate)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        // navigation buttons
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
                ) { Text("Back") }
            Button(onClick = {
                if (selectedRate != null) {
                    navController.navigate("summary")
                }
            },colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            ), enabled = selectedRate != null) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun WeightLossRateOption(rate: Float, selected: Float?, onSelect: (Float) -> Unit) {
    // single radio button option for weight loss rate
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected == rate,
            onClick = { onSelect(rate) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF1976D2)))
        Text("$rate kg/week")
    }
}
