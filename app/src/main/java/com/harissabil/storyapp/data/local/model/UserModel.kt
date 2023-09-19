package com.harissabil.storyapp.data.local.model

data class UserModel(
    val userId: String,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean
)