package com.harissabil.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.harissabil.storyapp.data.local.UserPreference
import com.harissabil.storyapp.data.local.model.UserModel
import com.harissabil.storyapp.data.remote.api.ApiService
import com.harissabil.storyapp.data.remote.response.ListStoryItem
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {


    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun login(email: String, password: String) =
        apiService.login(email, password)

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(token, apiService)
            }
        ).liveData
    }

    suspend fun getStoriesLocation(token: String) = apiService.getStoriesLocation(token)

    suspend fun addNewStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null
    ) =
        apiService.addNewStory(token, imageMultipart, description, lat, lon)

    fun getUser() = userPreference.getUser().asLiveData()

    suspend fun saveUser(user: UserModel) = userPreference.saveUser(user)

    suspend fun logout() = userPreference.logout()

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}