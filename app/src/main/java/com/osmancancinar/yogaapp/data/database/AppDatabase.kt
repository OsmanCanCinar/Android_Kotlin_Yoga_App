package com.osmancancinar.yogaapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.osmancancinar.yogaapp.data.model.Blog
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.data.model.Yoga

@Database(
    entities = [MeditationCategoriesList::class, MeditationCategory::class, Yoga::class, Blog::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {


    //In order to call our queries we reference an abstract Database Access Object.
    abstract fun appDao(): AppDao


    //We are using Singleton Pattern for our database. Only one instance of it can be created and used, which we do it within.
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val lock = Any()


        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock) {
            INSTANCE ?: makeAppDatabase(context).also {
                INSTANCE = it
            }
        }


        private fun makeAppDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "app_database"
        ).fallbackToDestructiveMigration().build()

    }
}