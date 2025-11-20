package dev.lunaris.app.ui.navigation

sealed class Screen(val route: String){
    //aca definimos las rutas
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Board : Screen("board")
    object Project : Screen("project")
    object ProjectDetail : Screen("projectDetail/{projectId}") {
        fun createRoute(projectId: String) = "projectDetail/$projectId"
    }
}