package com.example.nutrirateapp.data.retrofitAPI

import com.example.nutrirateapp.data.inteceptor.LoginInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIconfig {
    private const val BASE_URL = "https://nutrition-api-162761754517.asia-southeast2.run.app/"

    fun getApiService(token: String = ""): APIservice {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .apply {
                if (token.isNotEmpty()) {
                    addInterceptor(LoginInterceptor(token))
                }
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(APIservice::class.java)
    }
}