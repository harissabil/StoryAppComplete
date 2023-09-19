package com.harissabil.storyapp.ui.register

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.harissabil.storyapp.R
import com.harissabil.storyapp.databinding.ActivityRegisterBinding
import com.harissabil.storyapp.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"
    }

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.registerResponse.observe(this) {
            if (it.error) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Register success", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            Log.d(TAG, "btnRegister clicked")
            if (binding.etName.text.toString().isEmpty()) {
                binding.tilName.error = resources.getString(R.string.name_empty)
            } else if (binding.etEmail.text.toString().isEmpty()) {
                binding.tilEmail.error = resources.getString(R.string.email_empty)
            } else if (binding.etPassword.text.toString().isEmpty()) {
                binding.tilPassword.error = resources.getString(R.string.password_empty)
            } else {
                if (binding.tilPassword.error == null && binding.tilEmail.error == null) {
                    Log.d(TAG, "errorEnabled: ${binding.tilPassword.error}")
                    registerViewModel.register(
                        binding.etName.text.toString(),
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