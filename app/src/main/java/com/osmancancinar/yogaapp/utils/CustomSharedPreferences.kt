package com.osmancancinar.yogaapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPreferences {

    companion object {

        private const val PREFERENCES_LIST = "preferences_list"
        private var sharedPreferences: SharedPreferences? = null
        private lateinit var editor: SharedPreferences.Editor
        private var lock = Any()

        @Volatile
        private var instance: CustomSharedPreferences? = null

        operator fun invoke(context: Context): CustomSharedPreferences =
            instance ?: synchronized(lock) {
                instance ?: makeCustomSharedPreferences(context).also {
                    instance = it
                }
            }

        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferences {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }
    }

    fun saveList(list: String) {
        sharedPreferences?.edit(commit = true) {
            putString(PREFERENCES_LIST, list)
        }
    }

    fun clearList() {
        sharedPreferences?.edit(commit = true) {
            this.clear()
        }
    }

    fun getList() = sharedPreferences?.getString(PREFERENCES_LIST, "")
}