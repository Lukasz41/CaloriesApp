package co.uk.lmu.caloriesapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// singleton object that provides a configured retrofit instance for the food api
object RetrofitInstance {

    val api: FoodApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodApiService::class.java)
    }
}
