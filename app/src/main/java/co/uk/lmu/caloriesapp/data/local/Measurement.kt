package co.uk.lmu.caloriesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// entity for storing body weight measurements with a date
@Entity(tableName = "measurements")
data class Measurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val weight: Double
)