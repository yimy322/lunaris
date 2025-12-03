package dev.lunaris.app.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dev.lunaris.app.data.model.User

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
                            //guardar en Firestore
                            val userData = User(
                                id = user.uid,
                                email = email,
                                name = name,
                                photoUrl = user.photoUrl?.toString()
                            )
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(user.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    auth.signOut()
                                    currentUser = null
                                    isLoading = false
                                    onResult(true)
                                }
                                .addOnFailureListener {
                                    errorMessage = "Error al guardar usuario en Firestore"
                                    onResult(false)
                                }
                        }
                } else {
                    isLoading = false
                    errorMessage = getAuthErrorMessage(task.exception)
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
                    errorMessage = getAuthErrorMessage(task.exception)
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
    //para los errores
    private fun getAuthErrorMessage(e: Exception?): String {
        return when (e) {
            is FirebaseAuthWeakPasswordException ->
                "La contraseña debe tener al menos 6 caracteres."

            is FirebaseAuthInvalidCredentialsException ->
                "Correo o contraseña incorrectos."

            is FirebaseAuthUserCollisionException ->
                "Este correo ya está registrado."

            is FirebaseAuthInvalidUserException ->
                "No existe una cuenta con este correo."

            else -> "Ocurrió un error inesperado. Inténtalo nuevamente."
        }
    }
}