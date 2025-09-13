package co.uk.lmu.caloriesapp.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import co.uk.lmu.caloriesapp.data.remote.Food
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*


val primaryBackground = Color(0xFFF0F8FF)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel
) {
    val context = LocalContext.current
    // load user goals on initial launch
    LaunchedEffect(Unit) {
        viewModel.loadGoals(context)
    }
    val selectedDate by viewModel.selectedDate.collectAsState()
    // load meals whenever the selected date changes
    LaunchedEffect(selectedDate) {
        viewModel.loadMealsForDate(selectedDate)
    }

    val editingMeal = viewModel.editingMeal

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 96.dp)
            .verticalScroll(rememberScrollState())
        ) {
            // calendar selector
            CalendarBar(selectedDate = selectedDate, onDateSelected = viewModel::setSelectedDate)
            HorizontalDivider(thickness = 3.dp)
            Spacer(modifier = Modifier.height(16.dp))
            // meal list section for the selected date
            MealSection(selectedDate, viewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }

        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route ?: ""
        // bottom summary and navigation
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(primaryBackground)
        ) {
            HorizontalDivider(thickness = 3.dp)
            SummaryBar(viewModel = viewModel, selectedDate = selectedDate)
            HorizontalDivider(thickness = 3.dp)
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
        // open food search dialog if editing a meal
        if (editingMeal != null) {
            FoodSearchDialog(
                viewModel = viewModel,
                onDismiss = viewModel::stopEditingMeal,
                onFoodSelected = {}
            )
        }
    }
}


@Composable
fun CalendarBar(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val today = LocalDate.now()
    val days = remember {
        ( -3..3 ).map { today.plusDays(it.toLong()) }
    }

    val selectedDayTextColor = Color.White
    val unselectedDayTextColor = Color.Black
    val highlight = Color(0xFF64B5F6)
    // horizontal date selector
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryBackground)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(days) { date ->
            val isToday = date == today
            val isSelected = date == selectedDate

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDateSelected(date) }
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(1),
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            when {
                                isSelected -> highlight
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isSelected) selectedDayTextColor else unselectedDayTextColor
                    )
                }
            }
        }
    }
}

@Composable
fun MealSection(selectedDate: LocalDate, viewModel: DashboardViewModel) {
    val today = LocalDate.now()
    val isEditable = !selectedDate.isBefore(today)
    val meals = viewModel.meals[selectedDate] ?: listOf(
        Meal("Breakfast"), Meal("Snack 1"), Meal("Lunch"), Meal("Snack 2"), Meal("Dinner")
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        meals.forEach { meal ->
            MealRow(
                name = meal.name,
                foods = meal.items,
                editable = isEditable,
                onAddClick = { viewModel.startEditingMeal(meal.name) },
                viewModel = viewModel,
                selectedDate = selectedDate
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun MealRow(
    name: String,
    foods: List<TrackedFood>,
    editable: Boolean,
    onAddClick: () -> Unit,
    viewModel: DashboardViewModel,
    selectedDate: LocalDate
) {
    val mealHeaderColor = Color(0xFF1976D2)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, fontWeight = FontWeight.Bold, color = mealHeaderColor)

            if (editable) {
                IconButton(onClick = onAddClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add meal")
                }
            }
        }

        if (foods.isNotEmpty()) {
            foods.forEach { tracked ->
                val food = tracked.food
                val grams = tracked.grams
                val scale = grams / 100.0
                val nutrients = food.foodNutrients

                val kcal = ((nutrients.find { it.nutrientName.contains("Energy") }?.value?.toDouble() ?: 0.0) * scale).toInt()
                val carbs = ((nutrients.find { it.nutrientName.contains("Carbohydrate") }?.value?.toDouble() ?: 0.0) * scale).toInt()
                val fat = ((nutrients.find { it.nutrientName.contains("Total lipid") }?.value?.toDouble() ?: 0.0) * scale).toInt()
                val protein = ((nutrients.find { it.nutrientName.contains("Protein") }?.value?.toDouble() ?: 0.0) * scale).toInt()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${food.description} - $grams g")
                        Text("${kcal}kcal - ${protein}P - ${fat}F- ${carbs}C ", fontSize = 14.sp)
                    }

                    if (editable) {
                        IconButton(onClick = {
                            viewModel.removeFoodFromMeal(selectedDate, name, tracked)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            }
        } else {
            Text("No items yet", fontSize = 14.sp, color = Color.Gray)
        }

        if (!editable) {
            Text("(Past day)", fontSize = 12.sp, color = Color.Gray)
        }
    }
}



@Composable
fun FoodSearchDialog(
    viewModel: DashboardViewModel,
    onDismiss: () -> Unit,
    onFoodSelected: (Food) -> Unit
) {
    var query by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search food") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.searchFood(query) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2))
                    ) {
                    Text("Search")
                }
                Spacer(modifier = Modifier.height(8.dp))
                var selectedFood by remember { mutableStateOf<Food?>(null) }
                // dialog to confirm adding food with amount
                selectedFood?.let {
                    AddFoodDialog(
                        food = it,
                        onConfirm = { grams ->
                            onFoodSelected(it.copy()) // pass to caller
                            val editingFood = viewModel.foodBeingEdited
                            val mealName = viewModel.editingMeal ?: return@AddFoodDialog

                            if (editingFood != null) {
                                viewModel.removeFoodFromMeal(viewModel.selectedDate.value, mealName, editingFood)
                                viewModel.stopEditingFood()
                            }

                            viewModel.addFoodToMeal(viewModel.selectedDate.value, mealName, it, grams)
                            selectedFood = null
                            onDismiss()
                        },
                        onDismiss = { selectedFood = null }
                    )
                }
                // list of food results
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(viewModel.searchResults) { food ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFood = food }
                                .padding(vertical = 6.dp)
                        ) {
                            Text(text = food.description, fontWeight = FontWeight.Bold)

                            val energy = food.foodNutrients.find { it.nutrientName.contains("Energy") }?.value?.toInt() ?: 0
                            val protein = food.foodNutrients.find { it.nutrientName.contains("Protein") }?.value?.toInt() ?: 0
                            val fat = food.foodNutrients.find { it.nutrientName.contains("Total lipid") }?.value?.toInt() ?: 0
                            val carbs = food.foodNutrients.find { it.nutrientName.contains("Carbohydrate") }?.value?.toInt() ?: 0

                            Text(text = "$energy kcal - ${protein}P ${fat}F ${carbs}C", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

            }
        }
    )
}

@Composable
fun AddFoodDialog(
    food: Food,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var grams by remember { mutableStateOf("100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onConfirm(grams.toIntOrNull() ?: 100)
            },
                    colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2))) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2))) {
                Text("Cancel")
            }
        },
        title = { Text("Add ${food.description}") },
        text = {
            Column {
                Text("Enter amount in grams:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = grams,
                    onValueChange = { grams = it },
                    singleLine = true
                )
            }
        }
    )
}
@Composable
fun SummaryBar(viewModel: DashboardViewModel, selectedDate: LocalDate) {
    val summary = viewModel.getDailyNutrientSummary(selectedDate)
    val kcalColor = if (summary.kcal > viewModel.calorieGoal.intValue) Color(0xFFCC3333) else Color(0xFF4CAF50)
    // nutrient summary for the day
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Kcal ${summary.kcal}  Protein ${summary.protein}g  Fat ${summary.fat}g  Carbs ${summary.carbs}g",
            color = kcalColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "${viewModel.calorieGoal.intValue}    ${viewModel.proteinGoal.intValue}g   ${viewModel.fatGoal.intValue}g   ${viewModel.carbsGoal.intValue}g",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}



