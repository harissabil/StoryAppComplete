package com.harissabil.storyapp.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.harissabil.storyapp.data.StoryRepository
import com.harissabil.storyapp.data.remote.response.StoryResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _storyResponse = MutableLiveData<StoryResponse>()
    val storyResponse: LiveData<StoryResponse> = _storyResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoriesLocation(token: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.getStoriesLocation(token)
                Log.d(TAG, "onSuccess: ${response.message}")
                _isLoading.postValue(false)
                _storyResponse.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}