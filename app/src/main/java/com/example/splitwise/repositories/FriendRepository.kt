package com.example.splitwise.repositories

import com.example.splitwise.model.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FriendRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun addFriend(
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(Exception("User is not logged in"))
            return
        }

        val currentUserId = currentUser.uid
        val cleanName = name.trim()
        val cleanEmail = email.trim()

        if (cleanName.isBlank()) {
            onFailure(Exception("Name is required"))
            return
        }

        if (cleanEmail.isBlank()) {
            onFailure(Exception("Email is required"))
            return
        }

        val currentUserReference = firestore
            .collection("users")
            .document(currentUserId)

        // First find out whether this email already has a user document
        firestore
            .collection("users")
            .whereEqualTo("email", cleanEmail)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->

                // ------------------------------------------------
                // EMAIL ALREADY EXISTS IN USERS
                // ------------------------------------------------
                if (!snapshot.isEmpty) {

                    val friendDocument = snapshot.documents[0]
                    val friendId = friendDocument.id

                    // Cannot add yourself
                    if (friendId == currentUserId) {
                        onFailure(
                            Exception("You cannot add yourself as a friend")
                        )
                        return@addOnSuccessListener
                    }

                    // Get current user's friends
                    currentUserReference
                        .get()
                        .addOnSuccessListener { currentUserDocument ->

                            if (!currentUserDocument.exists()) {
                                onFailure(
                                    Exception("Current user profile not found")
                                )
                                return@addOnSuccessListener
                            }

                            val friendIds =
                                currentUserDocument.get("friends") as? List<String>
                                    ?: emptyList()

                            // Already a friend
                            if (friendIds.contains(friendId)) {
                                onFailure(
                                    Exception("This person is already your friend")
                                )
                                return@addOnSuccessListener
                            }

                            // Add existing user's UID
                            currentUserReference
                                .update(
                                    "friends",
                                    FieldValue.arrayUnion(friendId)
                                )
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

                } else {

                    // ------------------------------------------------
                    // EMAIL DOES NOT EXIST
                    // CREATE NEW USER
                    // ------------------------------------------------

                    val userReference = firestore
                        .collection("users")
                        .document()

                    val friendId = userReference.id

                    val newUser = User(
                        id = friendId,
                        name = cleanName,
                        email = cleanEmail,
                        friends = emptyList(),
                        createdAt = Timestamp.now()
                    )

                    val batch = firestore.batch()

                    // Create new user
                    batch.set(
                        userReference,
                        newUser
                    )

                    // Add new user's ID to current user's friends
                    batch.update(
                        currentUserReference,
                        "friends",
                        FieldValue.arrayUnion(friendId)
                    )

                    batch.commit()
                        .addOnSuccessListener {
                            onSuccess()
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
    fun addUserToGroup(
        groupId: String?,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(Exception("User is not logged in"))
            return
        }

        val cleanName = name.trim()
        val cleanEmail = email.trim()

        if (cleanName.isBlank()) {
            onFailure(Exception("Name is required"))
            return
        }

        if (cleanEmail.isBlank()) {
            onFailure(Exception("Email is required"))
            return
        }

        if (groupId.isNullOrBlank()) {
            onFailure(Exception("Group ID is missing"))
            return
        }

        firestore
            .collection("users")
            .whereEqualTo("email", cleanEmail)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->

                if (!snapshot.isEmpty) {
                    val userDocument = snapshot.documents[0]

                    val userId = userDocument.id
                    val userName =
                        userDocument.getString("name") ?: cleanName
                    val userEmail =
                        userDocument.getString("email") ?: cleanEmail

                    addExistingUserToGroup(
                        groupId = groupId,
                        userId = userId,
                        name = userName,
                        email = userEmail,
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                } else {
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
        groupId: String?,
        userId: String,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(Exception("User is not logged in"))
            return
        }

        if (groupId.isNullOrBlank()) {
            onFailure(Exception("Group ID is missing"))
            return
        }

        val currentUserId = currentUser.uid

        if (currentUserId == userId) {
            onFailure(Exception("You cannot add yourself to the group"))
            return
        }

        val groupReference = firestore
            .collection("groups")
            .document(groupId)

        val currentUserReference = firestore
            .collection("users")
            .document(currentUserId)

        val memberReference = groupReference
            .collection("members")
            .document(userId)

        groupReference
            .get()
            .addOnSuccessListener { groupDocument ->

                if (!groupDocument.exists()) {
                    onFailure(Exception("Group not found"))
                    return@addOnSuccessListener
                }

                val memberIds =
                    groupDocument.get("memberIds") as? List<String>
                        ?: emptyList()

                if (memberIds.contains(userId)) {
                    onFailure(
                        Exception("This person is already a member of this group")
                    )
                    return@addOnSuccessListener
                }

                val batch = firestore.batch()

                batch.update(
                    groupReference,
                    "memberIds",
                    FieldValue.arrayUnion(userId)
                )

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

                batch.update(
                    currentUserReference,
                    "friends",
                    FieldValue.arrayUnion(userId)
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
        groupId: String?,
        name: String,
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(Exception("User is not logged in"))
            return
        }

        if (groupId.isNullOrBlank()) {
            onFailure(Exception("Group ID is missing"))
            return
        }

        val currentUserId = currentUser.uid

        val groupReference = firestore
            .collection("groups")
            .document(groupId)

        val currentUserReference = firestore
            .collection("users")
            .document(currentUserId)

        groupReference
            .get()
            .addOnSuccessListener { groupDocument ->

                if (!groupDocument.exists()) {
                    onFailure(Exception("Group not found"))
                    return@addOnSuccessListener
                }

                val userReference = firestore
                    .collection("users")
                    .document()

                val userId = userReference.id

                val memberReference = groupReference
                    .collection("members")
                    .document(userId)

                val newUser = User(
                    id = userId,
                    name = name,
                    email = email,
                    friends = emptyList(),
                    createdAt = Timestamp.now()
                )

                val batch = firestore.batch()

                batch.set(
                    userReference,
                    newUser
                )

                batch.update(
                    groupReference,
                    "memberIds",
                    FieldValue.arrayUnion(userId)
                )

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

                batch.update(
                    currentUserReference,
                    "friends",
                    FieldValue.arrayUnion(userId)
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

    fun getFriends(
        onSuccess: (List<User>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(Exception("User is not logged in"))
            return
        }

        val currentUserId = currentUser.uid

        val currentUserReference = firestore
            .collection("users")
            .document(currentUserId)

        currentUserReference
            .get()
            .addOnSuccessListener { userDocument ->

                if (!userDocument.exists()) {
                    onSuccess(emptyList())
                    return@addOnSuccessListener
                }

                val friendIds =
                    userDocument.get("friends") as? List<String>
                        ?: emptyList()

                if (friendIds.isEmpty()) {
                    onSuccess(emptyList())
                    return@addOnSuccessListener
                }

                val requests = friendIds.map { friendId ->
                    firestore
                        .collection("users")
                        .document(friendId)
                        .get()
                }

                Tasks
                    .whenAllSuccess<com.google.firebase.firestore.DocumentSnapshot>(
                        requests
                    )
                    .addOnSuccessListener { documents ->

                        val friends = documents.mapNotNull { document ->

                            if (!document.exists()) {
                                return@mapNotNull null
                            }

                            document
                                .toObject(User::class.java)
                                ?.copy(id = document.id)
                        }

                        onSuccess(friends)
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