package co.uk.lmu.caloriesapp.data.local

import androidx.room.*

@Dao
interface MealDao {
    // insert a meal and return its id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long
    // insert a tracked food item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedFood(food: TrackedFoodEntity)
    // insert a meal and its related food items in a single transaction
    @Transaction
    suspend fun insertMealWithFoods(meal: MealEntity, foods: List<TrackedFoodEntity>) {
        val mealId = insertMeal(meal)
        foods.forEach {
            insertTrackedFood(it.copy(mealId = mealId.toInt()))
        }
    }
    // get all meals for a specific date
    @Query("SELECT * FROM meals WHERE date = :date")
    suspend fun getMealsForDate(date: String): List<MealEntity>
    // get all tracked foods for a given meal
    @Query("SELECT * FROM tracked_foods WHERE mealId = :mealId")
    suspend fun getTrackedFoodsForMeal(mealId: Int): List<TrackedFoodEntity>
    // delete all meals for a specific date
    @Query("DELETE FROM meals WHERE date = :date")
    suspend fun deleteMealsByDate(date: String)
    // delete all meals from the table
    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()
    // delete all tracked foods from the table
    @Query("DELETE FROM tracked_foods")
    suspend fun deleteAllTrackedFoods()
    // clear all meals and their food items
    @Transaction
    suspend fun clearAllMealsData() {
        deleteAllTrackedFoods()
        deleteAllMeals()
    }
}
