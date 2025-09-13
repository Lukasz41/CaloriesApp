package co.uk.lmu.caloriesapp.dashboard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.uk.lmu.caloriesapp.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// function that displays a confirmation dialog for resetting the app
@Composable
fun AppResetScreen(navController: NavController) {
    val context = LocalContext.current
    // remembered state to control whether the confirmation dialog is shown
    var showDialog = remember { mutableStateOf(true) }

    if (showDialog.value) {
        // show an alert dialog asking the user to confirm the reset
        AlertDialog(
            onDismissRequest = { navController.popBackStack() }, // go back if dialog is dismissed
            title = {
                Text("Confirm Reset")
            },
            text = {
                Text("Are you sure you want to reset the app and create a new account? All your saved data will be permanently deleted.")
            },
            confirmButton = {
                // "Yes, reset" button - triggers data deletion and navigation
                Button(
                    onClick = {
                        showDialog.value = false
                        // delete all user data from the local database
                        CoroutineScope(Dispatchers.IO).launch {
                            AppDatabase.getInstance(context).userDao().deleteAll()
                            AppDatabase.getInstance(context).shoppingItemDao().clearAll()
                            AppDatabase.getInstance(context).measurementDao().clearAll()
                            AppDatabase.getInstance(context).mealDao().clearAllMealsData()
                        }

                        // after a short delay, navigate to account creation screen
                        CoroutineScope(Dispatchers.Main).launch {
                            kotlinx.coroutines.delay(1500)
                            navController.navigate("create_account") {
                                popUpTo("dashboard") { inclusive = true } // clear backstack to prevent return to dashboard
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                ) {
                    Text("Yes, reset")
                }
            },
            dismissButton = {
                // "Cancel" button - go back to the previous screen
                TextButton(onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF1976D2)
                    )) {
                    Text("Cancel")
                }
            }
        )
    } else {
        // While the reset is in progress, show a placeholder message
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Resetting application...",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
