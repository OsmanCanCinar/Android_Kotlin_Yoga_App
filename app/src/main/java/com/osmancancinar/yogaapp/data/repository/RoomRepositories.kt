package com.osmancancinar.yogaapp.data.repository

import androidx.lifecycle.LiveData
import com.osmancancinar.yogaapp.data.database.AppDao
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.data.model.MeditationCategory

class RoomRepositories (private val dao: AppDao) {

    //Meditation Categories List Related
    //Deletes all of categories from the database with the use of Coroutines.
    suspend fun deleteMeditationCategoriesList() {
        dao.deleteMeditationCategoriesList()
    }


    //Inserts given list of categories to the database with the use of Coroutines.
    suspend fun insertMeditationCategoriesList(list: List<MeditationCategoriesList>) {
        dao.insertMeditationCategoriesList(list)
    }


    //Retrieves all the categories as a list and since it may change, we retrieved as live data and observe it.
    fun getMeditationCategoriesList(): LiveData<List<MeditationCategoriesList>> {
        return dao.getMeditationCategoriesList()
    }


    //Meditation Category Related
    suspend fun deleteMeditationCategory() {
        dao.deleteMeditationCategory()
    }


    suspend fun insertMeditationCategory(list: List<MeditationCategory>) {
        dao.insertMeditationCategory(list)
    }


    fun getMeditationCategory(): LiveData<List<MeditationCategory>> {
        return dao.getMeditationCategory()
    }


    //Meditation Detail Related
    suspend fun getSelectedMeditation(id: Int): MeditationCategory {
        return dao.getSelectedMeditation(id)
    }
}