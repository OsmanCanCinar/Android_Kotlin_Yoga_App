package com.osmancancinar.yogaapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.osmancancinar.yogaapp.data.repository.FirebaseRepositories
import com.osmancancinar.yogaapp.ui.view.listeners.FirebaseListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application, repository: FirebaseRepositories) :
    AndroidViewModel(application),
    CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    var firebaseListener: FirebaseListener? = null

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}