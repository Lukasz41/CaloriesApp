package co.uk.lmu.caloriesapp.registration.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.uk.lmu.caloriesapp.registration.RegistrationViewModel

@Composable
fun ActivityLevelScreen(navController: NavController, viewModel: RegistrationViewModel) {
    val selectedLevel by viewModel.activityLevel.collectAsState()
    // screen layout with activity level options and navigation buttons
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Select Activity Level")
            // activity level radio options
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ActivityOption("Light", selectedLevel, viewModel::setActivityLevel)
                ActivityOption("Moderate", selectedLevel, viewModel::setActivityLevel)
                ActivityOption("Heavy", selectedLevel, viewModel::setActivityLevel)
            }
        }
        item{
        Spacer(modifier = Modifier.height(24.dp))
        // navigation buttons: back and next
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
                ) { Text("Back") }
            Button(onClick = {
                if (selectedLevel != null) navController.navigate("goal_weight")
            },colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            ), enabled = selectedLevel != null) {
                Text("Next")
            }
        }
        }
    }
}

@Composable
fun ActivityOption(label: String, selected: String?, onSelect: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected == label,
            onClick = { onSelect(label) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF1976D2)))
        Text(label)
    }
}
