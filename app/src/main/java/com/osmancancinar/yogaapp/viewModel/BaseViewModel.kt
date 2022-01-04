package com.osmancancinar.yogaapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.osmancancinar.yogaapp.data.repository.UserRepositories
import com.osmancancinar.yogaapp.view.listeners.AuthListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application, repository: UserRepositories) :
    AndroidViewModel(application),
    CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    var authListener: AuthListener? = null

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}