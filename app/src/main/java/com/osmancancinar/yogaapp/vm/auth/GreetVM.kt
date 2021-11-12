package com.osmancancinar.yogaapp.vm.auth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class GreetVM() : ViewModel() {

    fun addToDb(email: String?, name: String?, imgUrl: String, database: FirebaseFirestore) {
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

                if (!users.contains(email)) {
                    println("Welcome!")
                    val user = hashMapOf(
                        "userName" to name,
                        "userEmail" to email,
                        "imgUrl" to imgUrl
                    )

                    database.collection("users")
                        .add(user)
                        .addOnFailureListener {
                            Log.w(ContentValues.TAG, "Error adding document", it)
                        }
                }
            }
    }
}