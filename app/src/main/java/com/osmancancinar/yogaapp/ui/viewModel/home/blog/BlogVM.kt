package com.osmancancinar.yogaapp.ui.viewModel.home.blog

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.osmancancinar.yogaapp.data.database.AppDatabase
import com.osmancancinar.yogaapp.data.model.Blog
import com.osmancancinar.yogaapp.data.repository.FirebaseRepositories
import com.osmancancinar.yogaapp.data.repository.RoomRepositories
import com.osmancancinar.yogaapp.ui.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

class BlogVM @ViewModelInject constructor(
    @Named("Application") private val app: Application,
    @Named("Repositories") private val firebaseRepositories: FirebaseRepositories
) : BaseViewModel(app, firebaseRepositories) {


    private val disposables = CompositeDisposable()
    private val dao = AppDatabase.invoke(app.applicationContext).appDao()
    private val roomRepositories = RoomRepositories(dao)

    fun displayBlogList() {
        firebaseListener?.onStarted()

        val disposable = firebaseRepositories.getBlogListFB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { firebaseListener?.onSuccess() },
                { firebaseListener?.onFailure(it.message!!) }
            )
        disposables.add(disposable)
    }

    fun getBlogList(): LiveData<List<Blog>> {
        return roomRepositories.getBlogList()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}