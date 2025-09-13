package co.uk.lmu.caloriesapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [User::class, ShoppingItem::class, Measurement::class, MealEntity::class, TrackedFoodEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    // define access to each dao
    abstract fun userDao(): UserDao
    abstract fun shoppingItemDao(): ShoppingItemDao
    abstract fun measurementDao(): MeasurementDao
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        // provide a singleton instance of the database
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calories_db"
                )   .fallbackToDestructiveMigration() // reset db on version mismatch
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
