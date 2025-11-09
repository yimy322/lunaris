package dev.lunaris.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dev.lunaris.app.ui.navigation.LunarisNavGraph
import dev.lunaris.app.ui.theme.LunarisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LunarisTheme {
                val navController = rememberNavController()
                LunarisNavGraph(navController = navController)
            }
        }
    }
}