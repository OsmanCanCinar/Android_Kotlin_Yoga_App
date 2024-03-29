package com.osmancancinar.yogaapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.osmancancinar.yogaapp.data.model.Blog
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.data.model.Yoga

@Dao
interface AppDao {


    /**
     *Meditation Categories List Related
     */
    // I am inserting the list that holds the meditation categories which I retrieved from the Firebase-Firestore with Coroutines.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeditationCategoriesList(list: List<MeditationCategoriesList>)


    //Before Inserting the new list, I Clear the table in the Room Database.
    @Query("DELETE FROM meditation_categories_list_table")
    suspend fun deleteMeditationCategoriesList()


    //Retrieve the inserted meditation categories from the Room database.
    @Query("SELECT* FROM meditation_categories_list_table")
    fun getMeditationCategoriesList(): LiveData<List<MeditationCategoriesList>>


    /**
     *Meditation Category Related
     */
    @Query("DELETE FROM meditation_category_table")
    suspend fun deleteMeditationCategory()


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeditationCategory(list: List<MeditationCategory>)


    @Query("SELECT* FROM meditation_category_table")
    fun getMeditationCategory(): LiveData<List<MeditationCategory>>


    /**
     *Meditation Detail Related
     */
    @Query("SELECT * FROM meditation_category_table WHERE meditationId = :meditationId")
    suspend fun getSelectedMeditation(meditationId: Int): MeditationCategory


    /**
     *Yoga Related
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertYogaList(list: List<Yoga>)


    @Query("DELETE FROM yoga_table")
    suspend fun deleteYogaList()


    @Query("SELECT* FROM yoga_table")
    fun getYogaList(): LiveData<List<Yoga>>


    @Query("SELECT * FROM yoga_table WHERE yogaId = :yogaId")
    suspend fun getSelectedYoga(yogaId: Int): Yoga


    /**
     *Blog Related
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBlogList(list: List<Blog>)


    @Query("DELETE FROM blog_table")
    suspend fun deleteBlogList()


    @Query("SELECT* FROM blog_table")
    fun getBlogList(): LiveData<List<Blog>>


    @Query("SELECT * FROM blog_table WHERE postId = :blogId")
    suspend fun getSelectedBlog(blogId: Int): Blog

}