package com.example.recetium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cloudinary.android.MediaManager
import com.example.recetium.ui.navigation.AppNavigation
import com.example.recetium.ui.theme.RecetiumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "recetium"
        config["api_key"] = "412324541887214"
        config["api_secret"] = "PtWkFuwZgMuf4bJL9rMWOCUQ6HY"

        MediaManager.init(this, config)

        setContent {
            RecetiumTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}