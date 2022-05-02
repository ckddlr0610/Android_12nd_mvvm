package com.example.week1.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.week1.data.dataclass.GithubUser
import com.example.week1.data.network.RetrofitService
import com.example.week1.databinding.ActivityRepoDetailBinding
import com.example.week1.domain.repository.RepoDetailRepository
import com.example.week1.presentation.viewmodel.RepoDetailViewModel

class RepoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepoDetailBinding
    private lateinit var viewModel: RepoDetailViewModel
    private lateinit var repoDetailRepository: RepoDetailRepository
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBar()

        if (intent.hasExtra("username")) {
            username = intent.getStringExtra("username").toString()
        } else {
            Log.d("Error", "intent has no extra")
            finish()
        }

        repoDetailRepository = RepoDetailRepository()

        viewModel = getViewModel(username)
        viewModel.githubRepoUser.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            // progressbar
            // progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
        })
    }

    private fun initActionBar() {
        setSupportActionBar(binding.detailBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun getViewModel(username: String): RepoDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RepoDetailViewModel(repoDetailRepository, username) as T
            }
        })[RepoDetailViewModel::class.java]
    }

    private fun bindUI(it: GithubUser) {
        with(binding) {
            Glide.with(root)
                .load(it.avatarUrl)
                .into(detailImg)
            detailName.text = it.login
        }
    }
}