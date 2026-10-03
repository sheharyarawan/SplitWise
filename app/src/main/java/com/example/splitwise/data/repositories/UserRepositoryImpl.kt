package com.example.splitwise.data.repositories
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.splitwise.data.model.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {
    override fun signInWithGoogle(
        context: Context,
        serverClientId: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(
                    context,
                    request
                )
                val credential = result.credential
                if (
                    credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCredential = GoogleIdTokenCredential.createFrom(
                        credential.data
                    )
                    val firebaseCredential = GoogleAuthProvider.getCredential(
                        googleCredential.idToken,
                        null
                    )
                    auth.signInWithCredential(firebaseCredential)
                        .addOnSuccessListener { authResult ->
                            val firebaseUser = authResult.user
                            if (firebaseUser != null) {
                                onSuccess(firebaseUser)
                            } else {
                                onFailure(Exception("Firebase user not found"))
                            }
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception)
                        }
                } else {
                    onFailure(Exception("Unexpected Google credential"))
                }
            } catch (exception: Exception) {
                onFailure(exception)
            }
        }
    }
    override fun signUpWithEmail(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    onSuccess(firebaseUser)
                } else {
                    onFailure(Exception("Firebase user not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    override fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    onSuccess(firebaseUser)
                } else {
                    onFailure(Exception("Firebase user not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    override fun getOrCreateUser(
        firebaseUser: FirebaseUser,
        name: String?,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val peopleReference = firestore.collection("people")
        peopleReference
            .whereEqualTo("uid", firebaseUser.uid)
            .limit(1)
            .get()
            .addOnSuccessListener { uidSnapshot ->
                if (!uidSnapshot.isEmpty) {
                    onSuccess(uidSnapshot.documents[0].id)
                    return@addOnSuccessListener
                }
                val email = firebaseUser.email?.trim()?.lowercase()
                if (email.isNullOrBlank()) {
                    onFailure(Exception("Account email not found"))
                    return@addOnSuccessListener
                }
                peopleReference
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { emailSnapshot ->
                        if (!emailSnapshot.isEmpty) {
                            val userDocument = emailSnapshot.documents[0]
                            val userId = userDocument.id
                            peopleReference
                                .document(userId)
                                .update(
                                    mapOf(
                                        "uid" to firebaseUser.uid,
                                        "isRegistered" to true
                                    )
                                )
                                .addOnSuccessListener {
                                    onSuccess(userId)
                                }
                                .addOnFailureListener { exception ->
                                    onFailure(exception)
                                }
                        } else {
                            val userReference = peopleReference.document()
                            val user = User(
                                id = userReference.id,
                                name = name ?: firebaseUser.displayName ?: "",
                                email = email,
                                uid = firebaseUser.uid,
                                isRegistered = true,
                                createdAt = Timestamp.now()
                            )
                            userReference
                                .set(user)
                                .addOnSuccessListener {
                                    onSuccess(userReference.id)
                                }
                                .addOnFailureListener { exception ->
                                    onFailure(exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    override fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}