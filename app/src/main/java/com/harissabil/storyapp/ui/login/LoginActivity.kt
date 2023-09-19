package com.harissabil.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.harissabil.storyapp.R
import com.harissabil.storyapp.databinding.ActivityLoginBinding
import com.harissabil.storyapp.ui.ViewModelFactory
import com.harissabil.storyapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupViewModel() {
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.registerResponse.observe(this) {
            if (it.error) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Toast.makeText(this, "Login success", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty()) {
                binding.tilEmail.error = resources.getString(R.string.email_empty)
            } else if (binding.etPassword.text.toString().isEmpty()) {
                binding.tilPassword.error = resources.getString(R.string.password_empty)
            } else {
                if (binding.tilPassword.error == null && binding.tilEmail.error == null) {
                    loginViewModel.login(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}