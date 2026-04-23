package com.example.lr26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.lr26.data.api.AuthApi
import com.example.lr26.data.api.NoteApi
import com.example.lr26.data.auth.TokenStorage
import com.example.lr26.data.local.NotesCache
import com.example.lr26.data.network.AuthInterceptor
import com.example.lr26.ui.navigation.AppNavHost
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenStorage = TokenStorage(this)
        val notesCache = NotesCache(this)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStorage))
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.250.169.220:3000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authApi = retrofit.create(AuthApi::class.java)
        val noteApi = retrofit.create(NoteApi::class.java)

        setContent {
            AppNavHost(
                navController = rememberNavController(),
                tokenStorage = tokenStorage,
                authApi = authApi,
                noteApi = noteApi,
                notesCache = notesCache
            )
        }
    }
}