package com.harissabil.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.harissabil.storyapp.R
import com.harissabil.storyapp.databinding.ActivityMainBinding
import com.harissabil.storyapp.ui.ViewModelFactory
import com.harissabil.storyapp.ui.adapter.LoadingStateAdapter
import com.harissabil.storyapp.ui.adapter.StoryListAdapter
import com.harissabil.storyapp.ui.add.AddActivity
import com.harissabil.storyapp.ui.maps.MapsActivity
import com.harissabil.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel(savedInstanceState)
        setupAction()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.rvStories.setHasFixedSize(false)
        binding.rvStories.isNestedScrollingEnabled = false
        binding.rvStories.layoutManager = LinearLayoutManager(this)
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        mainViewModel.getUser().observe(this) { user ->
            Log.d("MainActivity", "user: $user")
            if (user.isLogin) {
                binding.root.visibility = View.VISIBLE
                binding.tvWelcome.text = resources.getString(R.string.hello, user.name)

                if (savedInstanceState == null) {
//                    mainViewModel.getStories("Bearer ${user.token}")
                    fetchData("Bearer ${user.token}")
                }
//                mainViewModel.getStories("Bearer ${user.token}").observe(this) {
//                    adapter.submitData(lifecycle, it)
//                }
//
//                mainViewModel.errorResponse.observe(this) {
//                    binding.llError.visibility = View.VISIBLE
//                    binding.tvError.text = it
//                    binding.btnRetry.setOnClickListener {
//                        mainViewModel.getStories("Bearer ${user.token}").observe(this) {
//                            adapter.submitData(lifecycle, it)
//                        }
//                        binding.llError.visibility = View.GONE
//                    }
//                }

            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.logout()
            }
            R.id.action_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                mainViewModel.getUser().observe(this) { user ->
                    intent.putExtra(MapsActivity.EXTRA_TOKEN, user.token)
                }
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchData(token: String) {
        val adapter = StoryListAdapter()
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Loading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                    if (loadStates.refresh is LoadState.Error) {
                        if (adapter.itemCount < 1) {
                            binding.llError.visibility = View.VISIBLE
                            binding.btnRetry.setOnClickListener {
                                fetchData(token) // Retry fetching data
                            }
                        } else {
                            binding.llError.visibility = View.GONE
                        }
                    }
                }
            }
        }

        mainViewModel.getStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}