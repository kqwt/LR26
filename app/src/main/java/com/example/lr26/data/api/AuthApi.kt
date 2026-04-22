package com.example.lr26.data.api

import com.example.lr26.data.network.Resource
import retrofit2.http.*

interface AuthApi {
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Resource<String>
}