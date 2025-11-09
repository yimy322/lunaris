package dev.lunaris.app.ui.navigation

sealed class Screen(val route: String){
    //aca definimos las rutas
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Board : Screen("board")
}