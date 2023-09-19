package com.harissabil.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.harissabil.storyapp.data.StoryRepository
import com.harissabil.storyapp.data.local.model.UserModel
import com.harissabil.storyapp.data.remote.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _registerResponse = MutableLiveData<LoginResponse>()
    val registerResponse: LiveData<LoginResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                saveUser(
                    UserModel(
                        response.loginResult.userId,
                        response.loginResult.name,
                        email,
                        response.loginResult.token,
                        true
                    )
                )
                _isLoading.postValue(false)
                _registerResponse.postValue(response)
                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: HttpException) {
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                _registerResponse.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    private fun saveUser(user: UserModel) {
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }
}