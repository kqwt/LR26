package com.example.lr26.data.network

import com.example.lr26.data.auth.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenStorage: TokenStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenStorage.getToken()
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else chain.request()

        val response = chain.proceed(request)

        if (response.code == 401) {
            tokenStorage.clearToken()
        }
        return response
    }
}