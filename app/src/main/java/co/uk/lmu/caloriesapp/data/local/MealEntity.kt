package co.uk.lmu.caloriesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// entity representing a meal with a name and date, used to group tracked foods
@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val name: String
)
