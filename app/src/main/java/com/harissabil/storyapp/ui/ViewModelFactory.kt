package com.harissabil.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harissabil.storyapp.data.StoryRepository
import com.harissabil.storyapp.di.Injection
import com.harissabil.storyapp.ui.add.AddViewModel
import com.harissabil.storyapp.ui.login.LoginViewModel
import com.harissabil.storyapp.ui.main.MainViewModel
import com.harissabil.storyapp.ui.maps.MapsViewModel
import com.harissabil.storyapp.ui.register.RegisterViewModel

class ViewModelFactory(private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(AddViewModel::class.java) -> {
                AddViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(this.repository) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found: ${modelClass.name}")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}