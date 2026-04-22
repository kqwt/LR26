package com.example.lr26
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.lr26.ui.auth.AuthViewModel
import com.example.lr26.ui.navigation.AppNavHost
import androidx.compose.material3.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val authVM: AuthViewModel = viewModel()
                AppNavHost(navController, authVM)
            }
        }
    }
}