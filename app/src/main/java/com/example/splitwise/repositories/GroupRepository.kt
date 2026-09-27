package com.example.splitwise.repositories

import com.example.splitwise.model.Group
import com.example.splitwise.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.memoryEagerGcSettings

class GroupRepository {
    private val firestore= FirebaseFirestore.getInstance()
    private val auth= FirebaseAuth.getInstance()

    fun createGroup(
        name:String,
        type:String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        val currentUser= auth.currentUser

        if (currentUser == null) {
            onFailure(
                Exception("User is not logged in")
            )
            return
        }
        val userId= currentUser.uid

        val groupId= firestore.collection("groups")
            .document().id

        val group= Group(
            id = groupId,
            name= name,
            type= type,
            createdBy = userId,
            memberIds = listOf(userId),
            createdAt = Timestamp.now()
        )

        val groupRef= firestore.collection("groups")
            .document(groupId)

        val memberRef= groupRef.collection("members")
            .document(userId)

        val batch= firestore.batch()

        batch.set(groupRef,group)

        batch.set(
            memberRef,
            mapOf(
                "joinedAt" to Timestamp.now(),
                "role" to "admin"
            )
        )

        batch.commit()
            .addOnSuccessListener {
                onSuccess(groupId)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getGroups(
        onSuccess: (List<Group>) -> Unit,
        onFailure: (Exception) -> Unit) {

        val currentUser= auth.currentUser

        if (currentUser == null) {
            onFailure(
                Exception("User is not logged in")
            )
            return
        }
        firestore.collection("groups").whereArrayContains(
            "memberIds",
            currentUser.uid
        ).addSnapshotListener { snapshots, error ->
            if (error != null) {
                onFailure(error)
                return@addSnapshotListener
            }
            val groups= snapshots?.documents?.mapNotNull { document->
                document.toObject(Group::class.java)?.copy(
                    id = document.id
                )
            }?:emptyList()
            onSuccess(groups)
        }
    }

    fun getGroupById(
        groupId:String,
        onSuccess: (Group?) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        firestore
            .collection("groups")
            .document(groupId)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val group =
                        document
                            .toObject(Group::class.java)
                            ?.copy(
                                id = document.id
                            )

                    onSuccess(group)

                } else {

                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->

                onFailure(exception)
            }
    }

    fun getGroupMembers(
        groupId: String,
        onSuccess: (List<User>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        firestore
            .collection("groups")
            .document(groupId)
            .addSnapshotListener { document, exception ->

                if (exception != null) {
                    onFailure(exception)
                    return@addSnapshotListener
                }

                if (document == null || !document.exists()) {
                    onFailure(
                        Exception("Group not found")
                    )
                    return@addSnapshotListener
                }

                val memberIds =
                    document.get("memberIds")
                            as? List<String>
                        ?: emptyList()

                if (memberIds.isEmpty()) {
                    onSuccess(emptyList())
                    return@addSnapshotListener
                }

                val requests =
                    memberIds.map { userId ->

                        firestore
                            .collection("users")
                            .document(userId)
                            .get()
                    }

                com.google.android.gms.tasks.Tasks
                    .whenAllSuccess<com.google.firebase.firestore.DocumentSnapshot>(
                        requests
                    )
                    .addOnSuccessListener { documents ->

                        val members =
                            documents.mapNotNull { userDocument ->

                                userDocument
                                    .toObject(User::class.java)
                                    ?.copy(
                                        id = userDocument.id
                                    )
                            }

                        onSuccess(members)
                    }
                    .addOnFailureListener { exception ->

                        onFailure(exception)
                    }
            }
    }

}