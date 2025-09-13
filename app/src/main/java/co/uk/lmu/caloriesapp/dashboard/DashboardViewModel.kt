package co.uk.lmu.caloriesapp.dashboard

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.lmu.caloriesapp.data.local.MealDao
import co.uk.lmu.caloriesapp.data.local.MealEntity
import co.uk.lmu.caloriesapp.data.local.TrackedFoodEntity
import co.uk.lmu.caloriesapp.data.remote.Food
import co.uk.lmu.caloriesapp.data.remote.Nutrient
import co.uk.lmu.caloriesapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class Meal(
    val name: String,
    val items: List<TrackedFood> = emptyList()
)
data class TrackedFood(
    val food: Food,
    var grams: Int
)

class DashboardViewModel(private val dao: MealDao) : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    var searchResults = mutableStateListOf<Food>()
        private set

    private val _meals = mutableStateMapOf<LocalDate, List<Meal>>()
    val meals: SnapshotStateMap<LocalDate, List<Meal>> = _meals


    private var _editingMeal = mutableStateOf<String?>(null)
    val editingMeal: String? get() = _editingMeal.value

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }
    // search for foods using the api and update the results
    fun searchFood(query: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.searchFoods(query)
                Log.d("Search", "Results: ${result.foods.size}")
                searchResults.clear()
                searchResults.addAll(result.foods)
            } catch (e: Exception) {
                Log.e("Search", "Error: ${e.message}", e)
            }

        }
    }
    // begin editing a specific meal
    fun startEditingMeal(mealName: String) {
        _editingMeal.value = mealName
    }
    // stop editing meal
    fun stopEditingMeal() {
        _editingMeal.value = null
    }
    // add a food to the selected meal
    fun addFoodToMeal(date: LocalDate, mealName: String, food: Food, grams: Int) {
        val currentMeals = _meals[date].orEmpty()
        val updatedMeals = currentMeals.map {
            if (it.name == mealName) {
                it.copy(items = it.items + TrackedFood(food, grams))
            } else it
        }.ifEmpty {
            listOf("Breakfast", "Snack 1", "Lunch", "Snack 2", "Dinner").map { name ->
                if (name == mealName) Meal(name, listOf(TrackedFood(food, grams)))
                else Meal(name)
            }
        }

        _meals[date] = updatedMeals
        persistMealsForDate(date)
    }
    private var _foodBeingEdited = mutableStateOf<TrackedFood?>(null)
    val foodBeingEdited: TrackedFood? get() = _foodBeingEdited.value

    // remove a food item from a meal
    fun removeFoodFromMeal(date: LocalDate, mealName: String, food: TrackedFood) {
        val currentMeals = _meals[date].orEmpty()
        val updatedMeals = currentMeals.map {
            if (it.name == mealName) {
                it.copy(items = it.items - food)
            } else it
        }
        _meals[date] = updatedMeals
        persistMealsForDate(date)
    }
    // stop editing food
    fun stopEditingFood() {
        _foodBeingEdited.value = null
    }
    data class NutrientSummary(
        val kcal: Int,
        val protein: Int,
        val fat: Int,
        val carbs: Int
    )
    // calculate daily nutrient totals from all tracked foods
    fun getDailyNutrientSummary(date: LocalDate): NutrientSummary {
        val foods = meals[date]
            ?.flatMap { it.items }
            ?: emptyList()

        var kcal = 0
        var protein = 0
        var fat = 0
        var carbs = 0

        foods.forEach { tracked ->
            val scale = tracked.grams / 100.0
            val nutrients = tracked.food.foodNutrients

            val energy = nutrients.find { it.nutrientName.contains("Energy") }?.value?.toDouble() ?: 0.0
            val proteinVal = nutrients.find { it.nutrientName.contains("Protein") }?.value?.toDouble() ?: 0.0
            val fatVal = nutrients.find { it.nutrientName.contains("Total lipid") }?.value?.toDouble() ?: 0.0
            val carbsVal = nutrients.find { it.nutrientName.contains("Carbohydrate") }?.value?.toDouble() ?: 0.0

            kcal += (energy * scale).toInt()
            protein += (proteinVal * scale).toInt()
            fat += (fatVal * scale).toInt()
            carbs += (carbsVal * scale).toInt()
        }

        return NutrientSummary(kcal, protein, fat, carbs)
    }
    val calorieGoal = mutableIntStateOf(0)
    val proteinGoal = mutableIntStateOf(0)
    val fatGoal = mutableIntStateOf(0)
    val carbsGoal = mutableIntStateOf(0)

    // load user daily nutrient goals from shared preferences
    fun loadGoals(context: Context) {
        val prefs = context.getSharedPreferences("user_goals", Context.MODE_PRIVATE)

        calorieGoal.intValue = prefs.getInt("calorie_goal", 0)
        proteinGoal.intValue = prefs.getInt("protein_goal", 0)
        fatGoal.intValue = prefs.getInt("fat_goal", 0)
        carbsGoal.intValue = prefs.getInt("carbs_goal", 0)
    }
    // save meals for the given date to database
    fun persistMealsForDate(date: LocalDate) {
        viewModelScope.launch {
            val mealList = _meals[date] ?: return@launch

            dao.deleteMealsByDate(date.toString()) // Clear old meals

            for (meal in mealList) {
                val foods = meal.items.map {
                    TrackedFoodEntity(
                        mealId = 0, // replaced inside DAO
                        foodName = it.food.description,
                        kcal = it.food.foodNutrients.find { n -> n.nutrientName.contains("Energy") }?.value?.toDouble() ?: 0.0,
                        protein = it.food.foodNutrients.find { n -> n.nutrientName.contains("Protein") }?.value?.toDouble() ?: 0.0,
                        fat = it.food.foodNutrients.find { n -> n.nutrientName.contains("Total lipid") }?.value?.toDouble() ?: 0.0,
                        carbs = it.food.foodNutrients.find { n -> n.nutrientName.contains("Carbohydrate") }?.value?.toDouble() ?: 0.0,
                        grams = it.grams
                    )
                }
                dao.insertMealWithFoods(
                    MealEntity(name = meal.name, date = date.toString()),
                    foods
                )
            }
        }
    }
    // load saved meals from database for the selected date
    fun loadMealsForDate(date: LocalDate) {
        viewModelScope.launch {
            val meals = dao.getMealsForDate(date.toString())
            val result = mutableListOf<Meal>()

            for (meal in meals) {
                val foods = dao.getTrackedFoodsForMeal(meal.id).map {
                    TrackedFood(
                        food = Food(description = it.foodName, foodNutrients = listOf(
                            Nutrient(nutrientName = "Energy", value = it.kcal.toFloat(), unitName = "kcal"),
                            Nutrient(nutrientName = "Protein", value = it.protein.toFloat(), unitName = "g"),
                            Nutrient(nutrientName = "Total lipid", value = it.fat.toFloat(), unitName = "g"),
                            Nutrient(nutrientName = "Carbohydrate", value = it.carbs.toFloat(), unitName = "g"),
                        )),
                        grams = it.grams
                    )
                }
                result.add(Meal(name = meal.name, items = foods))
            }

            _meals[date] = if (result.isEmpty()) {
                listOf("Breakfast", "Snack 1", "Lunch", "Snack 2", "Dinner").map { Meal(it) }
            } else result
        }
    }
}
