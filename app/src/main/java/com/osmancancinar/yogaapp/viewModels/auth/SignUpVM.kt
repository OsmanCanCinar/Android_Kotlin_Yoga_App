package com.osmancancinar.yogaapp.viewModels.auth

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.util.Patterns
import com.google.firebase.firestore.FirebaseFirestore
import com.osmancancinar.yogaapp.R
import com.osmancancinar.yogaapp.viewModels.BaseViewModel

class SignUpVM(private val app: Application) : BaseViewModel(app) {

    fun validateEmail(email: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return app.getString(R.string.error_msg_email)
        }
        return null
    }

    fun validatePassword(password: String): String? {
        when {
            password.length < 8 -> {
                return app.getString(R.string.error_msg_password)
            }
            !password.matches(".*[A-Z].*".toRegex()) -> {
                return app.getString(R.string.uppercase_error)
            }
            !password.matches(".*[a-z].*".toRegex()) -> {
                return app.getString(R.string.lowercase_error)
            }
            !password.matches(".*[@#\$%^&+].*".toRegex()) -> {
                return app.getString(R.string.special_case_error)
            }
            !password.matches(".*[0-9].*".toRegex()) -> {
                return app.getString(R.string.number_case_error)
            }
            else -> return null
        }
    }

    fun validateName(name: String): String? {
        if (name.length < 3) {
            return app.getString(R.string.error_msg_name_char)
        } else if (!name.matches("^[a-zA-Z]*$".toRegex())) {
            return app.getString(R.string.error_msg_name)
        } else return null
    }

    fun addToDatabase(email: String, username: String, database: FirebaseFirestore) {
        val user = hashMapOf(
            "userEmail" to email,
            "userName" to username
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