package co.uk.lmu.caloriesapp.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// entity representing a tracked food item linked to a meal by foreign key
@Entity(
    tableName = "tracked_foods",
    foreignKeys = [ForeignKey(
        entity = MealEntity::class,
        parentColumns = ["id"],
        childColumns = ["mealId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TrackedFoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealId: Int,
    val foodName: String,
    val kcal: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val grams: Int
)
