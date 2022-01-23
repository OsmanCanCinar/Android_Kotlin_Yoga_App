package com.osmancancinar.yogaapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.data.model.Yoga

//For now, Database only consists of Meditation Category object, and as we update it, we will increase the version number by 1.
@Database(
    entities = [MeditationCategoriesList::class, MeditationCategory::class, Yoga::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    //in order to cal our queries we reference an abstract Database Access Object.
    abstract fun appDao(): AppDao

    //we are using Singleton Pattern for our database. Only one instance of it can be created and used, which we do it within.
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