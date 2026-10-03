package com.example.splitwise.ui.viewModels
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.splitwise.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    enum class SignUpField {
        NAME,
        EMAIL,
        PASSWORD,
        GENERAL
    }
    fun signInWithGoogle(
        context: Context,
        serverClientId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        userRepository.signInWithGoogle(
            context = context,
            serverClientId = serverClientId,
            onSuccess = { firebaseUser ->
                userRepository.getOrCreateUser(
                    firebaseUser = firebaseUser,
                    onSuccess = onSuccess,
                    onFailure = onFailure
                )
            },
            onFailure = onFailure
        )
    }
    fun signUpWithEmail(
        name: String,
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (SignUpField, String) -> Unit
    ) {
        val cleanName = name.trim()
        val cleanEmail = email.trim().lowercase()
        if (cleanName.isBlank()) {
            onFailure(SignUpField.NAME, "Please enter your name")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            onFailure(SignUpField.EMAIL, "Please enter a valid email address")
            return
        }
        if (password.length < 8) {
            onFailure(SignUpField.PASSWORD, "Password must be at least 8 characters")
            return
        }
        if (!password.any { it.isDigit() }) {
            onFailure(SignUpField.PASSWORD, "Password must contain at least one number")
            return
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            onFailure(SignUpField.PASSWORD, "Password must contain at least one special character")
            return
        }
        userRepository.signUpWithEmail(
            email = cleanEmail,
            password = password,
            onSuccess = { firebaseUser ->
                userRepository.getOrCreateUser(
                    firebaseUser = firebaseUser,
                    name = cleanName,
                    onSuccess = onSuccess,
                    onFailure = { exception ->
                        onFailure(
                            SignUpField.GENERAL,
                            exception.message ?: "Failed to create user"
                        )
                    }
                )
            },
            onFailure = { exception ->
                onFailure(
                    SignUpField.GENERAL,
                    exception.message ?: "Sign up failed"
                )
            }
        )
    }
    fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val cleanEmail = email.trim().lowercase()
        if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            onFailure("Please enter a valid email address")
            return
        }
        if (password.isBlank()) {
            onFailure("Please enter your password")
            return
        }
        userRepository.signInWithEmail(
            email = cleanEmail,
            password = password,
            onSuccess = { firebaseUser ->
                userRepository.getOrCreateUser(
                    firebaseUser = firebaseUser,
                    onSuccess = onSuccess,
                    onFailure = { exception ->
                        onFailure(exception.message ?: "Failed to load user")
                    }
                )
            },
            onFailure = { exception ->
                onFailure(exception.message ?: "Login failed")
            }
        )
    }
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val cleanEmail = email.trim().lowercase()
        if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            onFailure("Please enter a valid email address")
            return
        }
        userRepository.sendPasswordResetEmail(
            email = cleanEmail,
            onSuccess = onSuccess,
            onFailure = { exception ->
                onFailure(exception.message ?: "Failed to send password reset email")
            }
        )
    }
}