package com.osmancancinar.yogaapp.data.repository

import androidx.lifecycle.LiveData
import com.osmancancinar.yogaapp.data.database.AppDao
import com.osmancancinar.yogaapp.data.model.Blog
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.data.model.Yoga

class RoomRepositories(private val dao: AppDao) {


    /**
     *Meditation Categories List Related
     */
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


    /**
     *Meditation Category Related
     */
    suspend fun deleteMeditationCategory() {
        dao.deleteMeditationCategory()
    }


    suspend fun insertMeditationCategory(list: List<MeditationCategory>) {
        dao.insertMeditationCategory(list)
    }


    fun getMeditationCategory(): LiveData<List<MeditationCategory>> {
        return dao.getMeditationCategory()
    }


    /**
     *Meditation Detail Related
     */
    suspend fun getSelectedMeditation(id: Int): MeditationCategory {
        return dao.getSelectedMeditation(id)
    }


    /**
     *Yoga Related
     */
    suspend fun deleteYogaList() {
        dao.deleteYogaList()
    }


    suspend fun insertYogaList(list: List<Yoga>) {
        dao.insertYogaList(list)
    }


    fun getYogaList(): LiveData<List<Yoga>> {
        return dao.getYogaList()
    }


    suspend fun getSelectedYoga(id: Int): Yoga {
        return dao.getSelectedYoga(id)
    }


    /**
     *Blog Related
     */
    suspend fun deleteBlogList() {
        dao.deleteBlogList()
    }


    suspend fun insertBlogList(list: List<Blog>) {
        dao.insertBlogList(list)
    }


    fun getBlogList(): LiveData<List<Blog>> {
        return dao.getBlogList()
    }


    suspend fun getSelectedBlog(id: Int): Blog {
        return dao.getSelectedBlog(id)
    }

}