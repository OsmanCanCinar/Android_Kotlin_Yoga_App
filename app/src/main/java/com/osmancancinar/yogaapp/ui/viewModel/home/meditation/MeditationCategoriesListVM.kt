package com.osmancancinar.yogaapp.ui.viewModel.home.meditation

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.osmancancinar.yogaapp.data.database.AppDatabase
import com.osmancancinar.yogaapp.data.model.MeditationCategoriesList
import com.osmancancinar.yogaapp.data.repository.FirebaseRepositories
import com.osmancancinar.yogaapp.data.repository.RoomRepositories
import com.osmancancinar.yogaapp.ui.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

class MeditationCategoriesListVM @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val firebaseRepositories: FirebaseRepositories
) : BaseViewModel(app, firebaseRepositories) {

    private val disposables = CompositeDisposable()
    private val dao = AppDatabase.invoke(app.applicationContext).appDao()
    private val roomRepositories = RoomRepositories(dao)

    init {
        displayMeditationCategoriesList()
    }

    private fun displayMeditationCategoriesList() {
        firebaseListener?.onStarted()

        val disposable = firebaseRepositories.getMeditationCategoriesListFB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { firebaseListener?.onSuccess() },
                { firebaseListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)
    }

    fun getMeditationCategoriesList(): LiveData<List<MeditationCategoriesList>> {
        return roomRepositories.getMeditationCategoriesList()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}