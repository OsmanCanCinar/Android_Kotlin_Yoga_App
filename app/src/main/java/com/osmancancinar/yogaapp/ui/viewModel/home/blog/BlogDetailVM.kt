package com.osmancancinar.yogaapp.ui.viewModel.home.blog

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.osmancancinar.yogaapp.data.database.AppDatabase
import com.osmancancinar.yogaapp.data.model.Blog
import com.osmancancinar.yogaapp.data.repository.FirebaseRepositories
import com.osmancancinar.yogaapp.data.repository.RoomRepositories
import com.osmancancinar.yogaapp.ui.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Named

class BlogDetailVM @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val firebaseRepositories: FirebaseRepositories
) : BaseViewModel(app, firebaseRepositories) {

    private val dao = AppDatabase.invoke(app.applicationContext).appDao()
    private val roomRepositories = RoomRepositories(dao)

    val selectedPost = MutableLiveData<Blog>()

    fun getSelectedPost(id: Int) {
        launch {
            val blog = roomRepositories.getSelectedBlog(id)
            selectedPost.value = blog
        }
    }

}