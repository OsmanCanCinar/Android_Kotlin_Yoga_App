package com.osmancancinar.yogaapp.ui.viewModel.home.yoga

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osmancancinar.yogaapp.data.database.AppDatabase
import com.osmancancinar.yogaapp.data.model.Yoga
import com.osmancancinar.yogaapp.data.repository.FirebaseRepositories
import com.osmancancinar.yogaapp.data.repository.RoomRepositories
import com.osmancancinar.yogaapp.ui.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Named

class VideoActivityVM @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val firebaseRepositories: FirebaseRepositories
) : BaseViewModel(app, firebaseRepositories) {

    private val dao = AppDatabase.invoke(app.applicationContext).appDao()
    private val roomRepositories = RoomRepositories(dao)

    private val _uri = MutableLiveData<Uri>()

    fun uri(): LiveData<Uri> {
        return _uri
    }

    private lateinit var yoga: Yoga

    fun getSelectedYoga(id: Int) {
        launch {
            yoga = roomRepositories.getSelectedYoga(id)
            val convert: String = yoga.videoURL.toString()
            _uri.value = convert.toUri()
        }
    }
}