package co.uk.lmu.caloriesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// entity representing user profile and nutritional targets
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gender: String,
    val age: Int,
    val weight: Int,
    val height: Int,
    val activityLevel: String,
    val goalWeight: Int,
    val weightLossRate: Float,
    val calorieTarget: Int,
    val proteinTarget: Int,
    val fatTarget: Int,
    val carbsTarget: Int
)