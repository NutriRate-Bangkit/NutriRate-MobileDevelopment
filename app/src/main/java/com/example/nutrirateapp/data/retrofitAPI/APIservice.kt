package com.example.nutrirateapp.data.retrofitAPI

import com.example.nutrirateapp.data.model.LoginRequest
import com.example.nutrirateapp.data.model.LoginResponse
import com.example.nutrirateapp.data.model.RegisterRequest
import com.example.nutrirateapp.data.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface APIservice {
    @POST
    ("/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST
    ("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}