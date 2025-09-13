package co.uk.lmu.caloriesapp.registration.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.uk.lmu.caloriesapp.registration.RegistrationViewModel
import kotlin.math.abs
import kotlin.math.ceil

@Composable
fun SummaryScreen(navController: NavController, viewModel: RegistrationViewModel) {
    val calories = viewModel.calculateDailyCalories()
    val currentWeight = viewModel.weight.value ?: 0
    val goalWeight = viewModel.goalWeight.value ?: 0
    val lossRate = viewModel.weightLossRate.value ?: 0f

    // calculate macronutrient targets based on calorie distribution
    val carbs = (calories?.times(0.4)?.div(4))?.toInt() ?: 0 // 40% of calories, 4 kcal/g
    val protein = (calories?.times(0.3)?.div(4))?.toInt() ?: 0 // 30% of calories, 4 kcal/g
    val fat = (calories?.times(0.3)?.div(9))?.toInt() ?: 0     // 30% of calories, 9 kcal/g

    val weightDifference = abs(currentWeight - goalWeight)
    // estimate weeks needed to reach goal weight based on loss rate
    val weeksToGoal = if (lossRate > 0f) ceil(weightDifference / lossRate).toInt() else null

    // format estimated duration into weeks or months + weeks
    val timeEstimate = when {
        weeksToGoal == null -> "--"
        weeksToGoal < 4 -> "$weeksToGoal week(s)"
        else -> {
            val months = weeksToGoal / 4
            val weeksLeft = weeksToGoal % 4
            "$months month(s) ${if (weeksLeft > 0) ", $weeksLeft week(s)" else ""}"
        }
    }
    // main layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // calorie goal header
        Text("Your Daily Calorie Goal", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        // display total calories
        Text("Calories: $calories kcal", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        // display macronutrient breakdown
        Text("Macronutrient Breakdown:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Carbohydrates: $carbs g")
        Text("Protein: $protein g")
        Text("Fat: $fat g")

        Spacer(modifier = Modifier.height(24.dp))
        // display estimated time to reach the goal
        Text("Estimated time to reach your goal:", style = MaterialTheme.typography.titleMedium)
        Text(timeEstimate)

        Spacer(modifier = Modifier.height(32.dp))
        // navigation buttons
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { navController.navigate("create_account") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
                ) {
                Text("Start Again")
            }
            Button(onClick = {
                viewModel.computeTargets()
                viewModel.saveUserToDatabase()
                navController.navigate("dashboard")
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )) {
                Text("Save")
            }
        }
        val context = LocalContext.current
        // save target values to shared preferences
        LaunchedEffect(Unit) {
            viewModel.computeTargets()

            val prefs = context.getSharedPreferences("user_goals", Context.MODE_PRIVATE)
            prefs.edit()
                .putInt("calorie_goal", viewModel.calorieTarget.intValue)
                .putInt("protein_goal", viewModel.proteinTarget.intValue)
                .putInt("fat_goal", viewModel.fatTarget.intValue)
                .putInt("carbs_goal", viewModel.carbsTarget.intValue)
                .apply()
        }
    }
}
