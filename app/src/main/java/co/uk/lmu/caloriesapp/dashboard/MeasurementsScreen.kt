package co.uk.lmu.caloriesapp.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@SuppressLint("DefaultLocale")
@Composable
fun MeasurementsScreen(
    navController: NavController,
    viewModel: MeasurementsViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: ""

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 96.dp)
                .padding(16.dp)
        ) {
            // header row with title and add button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Body weight", fontSize = 20.sp)
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )

                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // list of measurements
            val measurements = viewModel.measurements.collectAsState().value

            LazyColumn {


                items(measurements) { measurement -> // ✅
                    val index = measurements.indexOfFirst { it.date == measurement.date }
                    val previous = measurements.getOrNull(index - 1)?.weight
                    val diff = if (previous != null) measurement.weight - previous else 0.0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = measurement.date,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${measurement.weight}kg",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = String.format("%.1f", diff),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFF1976D2))
                }
            }
            // show add dialog
            if (showDialog) {
                AddMeasurementDialog(
                    onSubmit = { date, weight ->
                        viewModel.addMeasurement(date, weight)
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }
        // bottom navigation bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            HorizontalDivider(thickness = 1.dp)
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}


@Composable
fun AddMeasurementDialog(
    onSubmit: (String, Double) -> Unit,
    onDismiss: () -> Unit
) {
    var date by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    // dialog to input date and weight
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Measurement") },
        text = {
            Column {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (dd.mm.yyyy)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2)
                    )
                )
            }
        },
        confirmButton = {
            // submit button to add the measurement
            Button(onClick = {
                val parsedWeight = weight.toDoubleOrNull()
                if (parsedWeight != null && date.isNotBlank()) {
                    onSubmit(date, parsedWeight)
                }
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2))) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF1976D2)
                )
            ) {
                Text("Cancel")
            }
        }
    )
}


