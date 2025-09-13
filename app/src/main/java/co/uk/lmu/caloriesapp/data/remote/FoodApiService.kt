package co.uk.lmu.caloriesapp.data.remote

import co.uk.lmu.caloriesapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query
// retrofit service interface for searching foods using the api
interface FoodApiService {

    @GET("v1/foods/search")
    suspend fun searchFoods(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): FoodSearchResponse
}
