package com.example.splitwise.repositories

import com.example.splitwise.model.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FriendRepository {

    private val firestore =
        FirebaseFirestore.getInstance()


    fun addUserToGroup(
        groupId: String,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val cleanName =
            name.trim()

        val cleanEmail =
            email.trim()

        if (cleanName.isBlank()) {
            onFailure(
                Exception("Name is required")
            )
            return
        }

        if (cleanEmail.isBlank()) {
            onFailure(
                Exception("Email is required")
            )
            return
        }

        firestore
            .collection("users")
            .whereEqualTo("email", cleanEmail)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->

                if (!snapshot.isEmpty) {

                    // User already exists
                    val userDocument =
                        snapshot.documents[0]

                    val userId =
                        userDocument.id

                    addExistingUserToGroup(
                        groupId = groupId,
                        userId = userId,
                        name = userDocument.getString("name") ?: cleanName,
                        email = userDocument.getString("email") ?: cleanEmail,
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )

                } else {

                    // User does not exist
                    // Create a new user using the
                    // name and email provided by the UI.

                    createUserAndAddToGroup(
                        groupId = groupId,
                        name = cleanName,
                        email = cleanEmail,
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    private fun addExistingUserToGroup(
        groupId: String,
        userId: String,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val groupReference =
            firestore
                .collection("groups")
                .document(groupId)

        groupReference
            .get()
            .addOnSuccessListener { document ->

                if (!document.exists()) {
                    onFailure(
                        Exception("Group not found")
                    )
                    return@addOnSuccessListener
                }

                val memberIds =
                    document.get("memberIds")
                            as? List<String>
                        ?: emptyList()

                if (memberIds.contains(userId)) {
                    onFailure(
                        Exception(
                            "This person is already a member of this group"
                        )
                    )
                    return@addOnSuccessListener
                }

                val batch =
                    firestore.batch()

                batch.update(
                    groupReference,
                    "memberIds",
                    FieldValue.arrayUnion(userId)
                )

                val memberReference =
                    groupReference
                        .collection("members")
                        .document(userId)

                batch.set(
                    memberReference,
                    mapOf(
                        "userId" to userId,
                        "name" to name,
                        "email" to email,
                        "joinedAt" to Timestamp.now(),
                        "role" to "member"
                    )
                )

                batch.commit()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    private fun createUserAndAddToGroup(
        groupId: String,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val groupReference =
            firestore
                .collection("groups")
                .document(groupId)

        groupReference
            .get()
            .addOnSuccessListener { groupDocument ->

                if (!groupDocument.exists()) {
                    onFailure(
                        Exception("Group not found")
                    )
                    return@addOnSuccessListener
                }

                val userReference =
                    firestore
                        .collection("users")
                        .document()

                val userId =
                    userReference.id

                val user =
                    User(
                        id = userId,
                        name = name,
                        email = email,
                        createdAt = Timestamp.now()
                    )

                val batch =
                    firestore.batch()

                // Create user
                batch.set(
                    userReference,
                    user
                )

                // Add user to group
                batch.update(
                    groupReference,
                    "memberIds",
                    FieldValue.arrayUnion(userId)
                )

                // Create group member
                val memberReference =
                    groupReference
                        .collection("members")
                        .document(userId)

                batch.set(
                    memberReference,
                    mapOf(
                        "userId" to userId,
                        "name" to name,
                        "email" to email,
                        "joinedAt" to Timestamp.now(),
                        "role" to "member"
                    )
                )

                batch.commit()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}