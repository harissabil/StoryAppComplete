package com.harissabil.storyapp.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harissabil.storyapp.databinding.ActivityWelcomeBinding
import com.harissabil.storyapp.ui.login.LoginActivity
import com.harissabil.storyapp.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            Intent(this@WelcomeActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.btnLogin.setOnClickListener {
            Intent(this@WelcomeActivity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}