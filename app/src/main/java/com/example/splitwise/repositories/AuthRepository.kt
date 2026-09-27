package com.example.splitwise.repository

import com.example.splitwise.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {

            val credential = GoogleAuthProvider.getCredential(idToken, null)

            val authResult = auth
                .signInWithCredential(credential)
                .await()

            val firebaseUser = authResult.user
                ?: return Result.failure(
                    Exception("Authentication failed")
                )

            val userRef = firestore
                .collection("users")
                .document(firebaseUser.uid)

            val snapshot = userRef.get().await()

            val user = if (snapshot.exists()) {

                snapshot.toObject(User::class.java)?.copy(
                    id = firebaseUser.uid
                ) ?: createUser(firebaseUser.uid)

            } else {
                createUser(firebaseUser.uid)
            }

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun createUser(
        uid: String
    ): User {

        val firebaseUser = auth.currentUser
            ?: throw Exception("User not found")

        val user = User(
            id = uid,
            name = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: "",
            phoneNumber = firebaseUser.phoneNumber,
            createdAt = Timestamp.now()
        )

        firestore
            .collection("users")
            .document(uid)
            .set(user)
            .await()

        return user
    }

    fun getCurrentUser() = auth.currentUser

    fun signOut() {
        auth.signOut()
    }
}