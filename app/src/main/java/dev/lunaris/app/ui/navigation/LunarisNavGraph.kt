package dev.lunaris.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.lunaris.app.ui.screens.BoardScreen
import dev.lunaris.app.ui.screens.LoginScreen
import dev.lunaris.app.ui.screens.OnboardingScreen
import dev.lunaris.app.ui.screens.ProjectDetailScreen
import dev.lunaris.app.ui.screens.ProjectScreen
import dev.lunaris.app.ui.screens.RegisterScreen
import dev.lunaris.app.ui.screens.TaskScreen

@Composable
fun LunarisNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Board.route) {
            BoardScreen(navController)
        }
        composable(Screen.Project.route) {
            ProjectScreen(navController)
        }
        composable(Screen.ProjectDetail.route) {
            ProjectDetailScreen(navController)
        }
        composable(Screen.Task.route) {
            TaskScreen(navController)
        }
    }
}