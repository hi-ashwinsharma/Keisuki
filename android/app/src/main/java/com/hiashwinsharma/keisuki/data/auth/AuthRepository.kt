package com.hiashwinsharma.keisuki.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

data class AuthState(
    val user: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isAuthenticated: Boolean get() = user != null
    val displayName: String get() = user?.displayName ?: user?.email ?: "Guest"
    val photoUrl: String? get() = user?.photoUrl?.toString()
}

class AuthRepository(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val credentialManager = CredentialManager.create(context)

    private val _authState = MutableStateFlow(
        AuthState(user = firebaseAuth.currentUser)
    )
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _authState.value = _authState.value.copy(
                user = auth.currentUser,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    suspend fun signInWithGoogle(webClientId: String): Result<FirebaseUser> {
        _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response: GetCredentialResponse = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = response.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                val user = authResult.user ?: throw IllegalStateException("Firebase user is null")
                _authState.value = AuthState(user = user)
                Result.success(user)
            } else {
                val err = "Unrecognized credential type: ${credential.type}"
                _authState.value = _authState.value.copy(isLoading = false, errorMessage = err)
                Result.failure(IllegalArgumentException(err))
            }
        } catch (e: GoogleIdTokenParsingException) {
            val err = "Google ID token parsing error: ${e.message}"
            _authState.value = _authState.value.copy(isLoading = false, errorMessage = err)
            Result.failure(e)
        } catch (e: Exception) {
            val err = e.localizedMessage ?: "Sign-in failed"
            _authState.value = _authState.value.copy(isLoading = false, errorMessage = err)
            Result.failure(e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _authState.value = AuthState(user = null)
    }
}
