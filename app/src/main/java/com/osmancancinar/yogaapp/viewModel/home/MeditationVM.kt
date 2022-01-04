package com.osmancancinar.yogaapp.viewModel.home

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.osmancancinar.yogaapp.data.model.MeditationCategory
import com.osmancancinar.yogaapp.data.repository.UserRepositories
import com.osmancancinar.yogaapp.utils.CustomSharedPreferences
import com.osmancancinar.yogaapp.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

class MeditationVM @ViewModelInject constructor(
    @Named("Application" )private val app: Application,
    @Named("Repositories") private val repository: UserRepositories) : BaseViewModel(app, repository) {

    private val disposables = CompositeDisposable()

    val auth = repository.getFirebaseAuthRef()

    var list: ArrayList<MeditationCategory> = arrayListOf()

    fun displayMeditationCategoriesList(){
        authListener?.onStarted()

        val disposable = repository.displayMeditationCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {list = convert(); authListener?.onSuccess()  },
                { authListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)
    }

    fun convert(): ArrayList<MeditationCategory> {
        val customPreferences = CustomSharedPreferences(app.applicationContext)
        val gson = Gson()
        val jsonList: String? = customPreferences.getList()
        val type = object : TypeToken<ArrayList<MeditationCategory>>() {}.type
        return gson.fromJson(jsonList, type)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
