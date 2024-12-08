package com.example.nutrirateapp.data.retrofitAPI

import com.example.nutrirateapp.data.RegisterRequest
import com.example.nutrirateapp.data.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface APIservice {
    @POST
    ("/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse
}