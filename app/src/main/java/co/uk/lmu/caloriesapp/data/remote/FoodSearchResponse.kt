package co.uk.lmu.caloriesapp.data.remote

// data models for parsing food search responses from the api
data class FoodSearchResponse(
    val foods: List<Food>
)

data class Food(
    val description: String,
    val foodNutrients: List<Nutrient>
)

data class Nutrient(
    val nutrientName: String,
    val value: Float,
    val unitName: String
)

