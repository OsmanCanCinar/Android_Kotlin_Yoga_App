package com.osmancancinar.yogaapp.viewModels.auth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class GreetVM() : ViewModel() {

    fun addToDb(email: String?, username: String?, database: FirebaseFirestore) {
        database.collection("users")
            .whereEqualTo("userEmail", email)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val users = ArrayList<String>()

                for (doc in value!!) {
                    doc.getString("userEmail")?.let {
                        users.add(it)
                    }
                }

                if (users.contains(email)) {
                    println("Welcome back!")
                } else {
                    println("Welcome!")
                    val user = hashMapOf(
                        "userName" to username,
                        "userEmail" to email
                    )

                    database.collection("users")
                        .add(user)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
                        }
                        .addOnFailureListener {
                            Log.w(ContentValues.TAG, "Error adding document", it)
                        }
                }
            }
    }
}