package com.example.splitwise.repositories

import com.example.splitwise.model.Group
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
        ).get().addOnSuccessListener { snapshots ->
            val groups= snapshots.documents.mapNotNull { document->
                document.toObject(Group::class.java)?.copy(
                    id = document.id
                )
            }
            onSuccess(groups)
        }.addOnFailureListener { exception ->

            onFailure(exception)
        }
    }
}