package co.uk.lmu.caloriesapp.registration

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.uk.lmu.caloriesapp.data.local.AppDatabase
import co.uk.lmu.caloriesapp.data.local.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : AndroidViewModel(application)
 {
     // get access to user dao from database
    private val userDao = AppDatabase.getInstance(application).userDao()
     // user input states wrapped in mutable state flows
    private val _gender = MutableStateFlow<String?>(null)
    val gender: StateFlow<String?> = _gender

    private val _age = MutableStateFlow<Int?>(null)
    val age: StateFlow<Int?> = _age

    private val _weight = MutableStateFlow<Int?>(null)
    val weight: StateFlow<Int?> = _weight

    private val _height = MutableStateFlow<Int?>(null)
    val height: StateFlow<Int?> = _height

    private val _activityLevel = MutableStateFlow<String?>(null)
    val activityLevel: StateFlow<String?> = _activityLevel

    private val _goalWeight = MutableStateFlow<Int?>(null)
    val goalWeight: StateFlow<Int?> = _goalWeight

    private val _weightLossRate = MutableStateFlow<Float?>(null)
    val weightLossRate: StateFlow<Float?> = _weightLossRate

     // setters for each user input field
    fun setGender(value: String) {
        _gender.value = value
    }

    fun setAge(value: Int) {
        _age.value = value
    }

    fun setWeight(value: Int) {
        _weight.value = value
    }

    fun setHeight(value: Int) {
        _height.value = value
    }

    fun setActivityLevel(value: String) {
        _activityLevel.value = value
    }

    fun setGoalWeight(value: Int) {
        _goalWeight.value = value
    }

    fun setWeightLossRate(value: Float) {
        _weightLossRate.value = value
    }
     // calculates daily calorie requirement based on user data
    fun calculateDailyCalories(): Int? {
        val weight = weight.value
        val height = height.value
        val age = age.value
        val gender = gender.value
        val activity = activityLevel.value
        val rate = weightLossRate.value

        if (weight == null || height == null || age == null || gender == null || activity == null || rate == null) {
            return null
        }
         // calculate bmr based on gender
        val bmr = if (gender == "Male") {
            10 * weight + 6.25 * height - 5 * age + 5
        } else {
            10 * weight + 6.25 * height - 5 * age - 161
        }
         // adjust for activity level
        val activityFactor = when (activity) {
            "Light" -> 1.375
            "Moderate" -> 1.55
            "Heavy" -> 1.725
            else -> 1.0
        }

        val maintenanceCalories = bmr * activityFactor
        // reduce calories based on selected weight loss rate
        val deficit = when (rate) {
            0.25f -> 275
            0.5f -> 550
            0.75f -> 825
            1.0f -> 1100
            else -> 0
        }

        return (maintenanceCalories - deficit).toInt()
    }
    // macros calculated from calorie goal
    val calorieTarget = mutableIntStateOf(0)
    val proteinTarget = mutableIntStateOf(0)
    val fatTarget = mutableIntStateOf(0)
    val carbsTarget = mutableIntStateOf(0)
    // compute macro targets based on calorie result
    fun computeTargets() {
        val calories = calculateDailyCalories() ?: 0
        calorieTarget.intValue = calories
        proteinTarget.intValue = (calories * 0.3 / 4).toInt()
        fatTarget.intValue = (calories * 0.3 / 9).toInt()
        carbsTarget.intValue = (calories * 0.4 / 4).toInt()
    }
     // save user data into database
     fun saveUserToDatabase() {
         viewModelScope.launch {
             val user = User(
                 gender = gender.value ?: "",
                 age = age.value ?: 0,
                 weight = weight.value ?: 0,
                 height = height.value ?: 0,
                 activityLevel = activityLevel.value ?: "",
                 goalWeight = goalWeight.value ?: 0,
                 weightLossRate = weightLossRate.value ?: 0f,
                 calorieTarget = calorieTarget.intValue,
                 proteinTarget = proteinTarget.intValue,
                 fatTarget = fatTarget.intValue,
                 carbsTarget = carbsTarget.intValue
             )
             userDao.insertUser(user)
         }
     }
     // check if a user is already registered in database
     fun isUserRegistered(callback: (Boolean) -> Unit) {
         viewModelScope.launch {
             val user = userDao.getUser()
             callback(user != null)
         }
     }
 }