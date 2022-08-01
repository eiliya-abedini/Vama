package com.vama.eiliyaabedini.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vama.eiliyaabedini.common.hide
import com.vama.eiliyaabedini.common.show
import com.vama.eiliyaabedini.databinding.ActivityTopAlbumsBinding
import com.vama.eiliyaabedini.presentation.adapter.MusicAlbumAdapter
import com.vama.eiliyaabedini.presentation.model.State
import com.vama.eiliyaabedini.presentation.viewmodel.TopAlbumsAction
import com.vama.eiliyaabedini.presentation.viewmodel.TopAlbumsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopAlbumsActivity : AppCompatActivity() {

    private val musicAlbumAdapter: MusicAlbumAdapter by lazy { MusicAlbumAdapter() }

    private val topAlbumsViewModel: TopAlbumsViewModel by viewModel()

    private val binding: ActivityTopAlbumsBinding by lazy { ActivityTopAlbumsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = musicAlbumAdapter

        topAlbumsViewModel.data.observe(this) { state ->
            when (state) {
                is State.DataState -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerView.show()
                    binding.errorView.hide()
                    musicAlbumAdapter.submitList(state.data)
                }

                is State.ErrorState -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.recyclerView.hide()
                    binding.errorView.show()
                    binding.errorMessageTextView.text = state.error
                }

                is State.LoadingState -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                is State.Events -> handleEvents(state.action)
            }
        }

        setListeners()

        topAlbumsViewModel.onViewCreated()
    }

    private fun handleEvents(action: TopAlbumsAction) {
        when (action) {
            is TopAlbumsAction.NavigateToAlbumDetail -> AlbumDetailActivity.launch(this, action.albumId)
        }
    }

    private fun setListeners() {
        musicAlbumAdapter.setOnItemClick { album ->
            topAlbumsViewModel.onMusicAlbumClick(album)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            topAlbumsViewModel.onSwipeRefresh()
        }

        binding.retryButton.setOnClickListener {
            topAlbumsViewModel.onRetry()
        }
    }
}


