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
fun AgeScreen(navController: NavController, viewModel: RegistrationViewModel) {
    val age by viewModel.age.collectAsState()
    var input by remember { mutableStateOf(age?.toString() ?: "") }
    val ageInt = input.toIntOrNull()
    // screen layout with age input and navigation buttons
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Enter Your Age")
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = input,
                onValueChange = {
                    if (it.length <= 3) input = it
                    ageInt?.let { value ->
                        if (value in 15..100) viewModel.setAge(value)
                    }
                },

                label = { Text("Age") }

            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                    ) { Text("Back") }
                Button(onClick = {
                    if (ageInt != null && ageInt in 15..100) {
                        viewModel.setAge(ageInt)
                        navController.navigate("weight")
                    }
                },colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                ), enabled = ageInt in 15..100) {
                    Text("Next")
                }
            }
        }
    }
}
