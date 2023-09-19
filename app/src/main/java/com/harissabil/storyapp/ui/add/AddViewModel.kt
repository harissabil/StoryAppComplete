package com.harissabil.storyapp.ui.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.harissabil.storyapp.data.StoryRepository
import com.harissabil.storyapp.data.local.model.UserModel
import com.harissabil.storyapp.data.remote.response.UploadResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _errorResponse = MutableLiveData<UploadResponse>()
    val errorResponse: LiveData<UploadResponse> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addNewStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.addNewStory(token, imageMultipart, description, lat, lon)
                _errorResponse.postValue(response)
                Log.d(TAG, "onSuccess: ${response.message}")
                _isLoading.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, UploadResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                _errorResponse.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getUser()
    }

    companion object {
        private const val TAG = "AddViewModel"
    }
}