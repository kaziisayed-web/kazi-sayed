package com.example.data.remote

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    data class Success(val user: FirebaseUser) : AuthResult()
    data class Error(val message: String, val requiresConsoleSetup: Boolean = false) : AuthResult()
}

object AuthService {
    private val auth: FirebaseAuth get() = Firebase.auth

    val currentUser: FirebaseUser? get() = auth.currentUser

    val isUserSignedIn: Boolean get() = auth.currentUser != null

    suspend fun registerWithEmail(email: String, password: String, displayName: String): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = result.user ?: return AuthResult.Error("Registration succeeded but user profile was null.")

            // Update display name
            if (displayName.isNotBlank()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName.trim())
                    .build()
                user.updateProfile(profileUpdates).await()
            }
            AuthResult.Success(user)
        } catch (e: FirebaseAuthException) {
            Log.e("AuthService", "Registration error code: ${e.errorCode}", e)
            val friendlyMsg = when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already registered. Please sign in."
                "ERROR_INVALID_EMAIL" -> "Invalid email address format."
                "ERROR_WEAK_PASSWORD" -> "Password is too weak. Please use at least 6 characters."
                "ERROR_OPERATION_NOT_ALLOWED" -> "Email/Password provider is not enabled in Firebase Console. Please enable it under Authentication > Sign-in method."
                else -> e.localizedMessage ?: "Registration failed."
            }
            AuthResult.Error(friendlyMsg, requiresConsoleSetup = e.errorCode == "ERROR_OPERATION_NOT_ALLOWED")
        } catch (e: Exception) {
            Log.e("AuthService", "Unexpected registration error", e)
            AuthResult.Error(e.localizedMessage ?: "Registration failed.")
        }
    }

    suspend fun loginWithEmail(email: String, password: String): AuthResult {
        return try {
            val credential = EmailAuthProvider.getCredential(email.trim(), password)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user ?: return AuthResult.Error("Sign in succeeded but user was null.")
            AuthResult.Success(user)
        } catch (e: FirebaseAuthException) {
            Log.e("AuthService", "Sign-in error code: ${e.errorCode}", e)
            val friendlyMsg = when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> "No account found with this email. Please register first."
                "ERROR_WRONG_PASSWORD" -> "Incorrect password. Please try again or reset password."
                "ERROR_INVALID_EMAIL" -> "Invalid email address format."
                "ERROR_OPERATION_NOT_ALLOWED" -> "Email/Password provider is not enabled in Firebase Console. Please enable it in the console."
                else -> e.localizedMessage ?: "Sign in failed."
            }
            AuthResult.Error(friendlyMsg, requiresConsoleSetup = e.errorCode == "ERROR_OPERATION_NOT_ALLOWED")
        } catch (e: Exception) {
            Log.e("AuthService", "Unexpected login error", e)
            AuthResult.Error(e.localizedMessage ?: "Sign in failed.")
        }
    }

    suspend fun sendPasswordReset(email: String): Result<String> {
        return try {
            auth.sendPasswordResetEmail(email.trim()).await()
            Result.success("Password reset email sent to $email. Please check your inbox.")
        } catch (e: Exception) {
            Log.e("AuthService", "Password reset failed", e)
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
