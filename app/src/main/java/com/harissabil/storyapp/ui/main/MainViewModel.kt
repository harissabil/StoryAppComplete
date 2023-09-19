package com.harissabil.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.harissabil.storyapp.data.StoryRepository
import com.harissabil.storyapp.data.local.model.UserModel
import com.harissabil.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) :
    ViewModel() {

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return repository.getStories(token).cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getUser()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}