package dev.lunaris.app.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

class AuthViewModel : ViewModel() {
    //conexion
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //usuario actual, el private set es es como el private de java
    var currentUser by mutableStateOf<FirebaseUser?>(auth.currentUser)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    //registrar
    fun register(name: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        isLoading = true
        errorMessage = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    //usuario recien creado
                    val user = auth.currentUser
                    //actualizamos el perfil de usuario para guardar el nombre
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }
                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener {
                            //cerramos sesion
                            auth.signOut()
                            currentUser = null
                            isLoading = false
                            onResult(true)
                        }
                } else {
                    isLoading = false
                    errorMessage = task.exception?.localizedMessage ?: "Error desconocido"
                    onResult(false)
                }
            }
    }
    //login
    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        isLoading = true
        errorMessage = null
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    onResult(true)
                } else {
                    errorMessage = task.exception?.localizedMessage ?: "Error desconocido"
                    onResult(false)
                }
            }
    }
    //salir
    fun signOut() {
        auth.signOut()
        currentUser = null
    }
    //refrescar usuario
    fun refreshUser() {
        currentUser = auth.currentUser
    }
}