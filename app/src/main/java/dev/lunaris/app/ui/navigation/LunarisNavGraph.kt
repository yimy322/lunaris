package dev.lunaris.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.lunaris.app.ui.screens.BoardScreen
import dev.lunaris.app.ui.screens.LoginScreen
import dev.lunaris.app.ui.screens.OnboardingScreen
import dev.lunaris.app.ui.screens.ProjectDetailScreen
import dev.lunaris.app.ui.screens.ProjectScreen
import dev.lunaris.app.ui.screens.RegisterScreen

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
        //este se tiene que pasa un parametro
        composable(
            route = Screen.ProjectDetail.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")!!
            ProjectDetailScreen(navController, projectId)
        }
    }
}