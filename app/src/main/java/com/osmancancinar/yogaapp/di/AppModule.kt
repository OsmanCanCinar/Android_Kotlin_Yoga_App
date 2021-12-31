package com.osmancancinar.yogaapp.di

import android.app.Application
import android.content.Context
import androidx.navigation.Navigator
import com.osmancancinar.yogaapp.BaseApplication
import com.osmancancinar.yogaapp.data.firebase.FirebaseSource
import com.osmancancinar.yogaapp.data.repositories.UserRepositories
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBaseApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    @Named("Application")
    fun provideApplication(app: Application): Application {
        return app
    }

    @Singleton
    @Provides
    @Named("Repositories")
    fun provideRepository(repository: UserRepositories): UserRepositories {
        return repository
    }

    @Singleton
    @Provides
    @Named("Source")
    fun provideSource(source: FirebaseSource): FirebaseSource {
        return source
    }

}