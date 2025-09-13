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
fun GenderScreen(navController: NavController, viewModel: RegistrationViewModel) {
    val selectedGender by viewModel.gender.collectAsState()
    // layout for selecting gender
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Gender")
        // gender radio button options
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            GenderOption("Male", selectedGender, viewModel::setGender)
            GenderOption("Female", selectedGender, viewModel::setGender)
        }

        Spacer(modifier = Modifier.height(20.dp))
        // navigation buttons
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
                ) {
                Text("Back")
            }
            Button(
                onClick = { navController.navigate("age") },
                enabled = selectedGender != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun GenderOption(label: String, selected: String?, onSelect: (String) -> Unit) {
    // single selection option with a radio button
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected == label,
            onClick = { onSelect(label) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF1976D2)))
        Text(label)
    }
}
