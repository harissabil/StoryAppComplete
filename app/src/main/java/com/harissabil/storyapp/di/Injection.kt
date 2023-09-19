package com.harissabil.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.harissabil.storyapp.data.StoryRepository
import com.harissabil.storyapp.data.local.UserPreference
import com.harissabil.storyapp.data.remote.api.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService, pref)
    }
}