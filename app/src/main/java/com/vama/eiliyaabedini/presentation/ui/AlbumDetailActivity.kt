package com.vama.eiliyaabedini.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vama.eiliyaabedini.R
import com.vama.eiliyaabedini.common.loadImage
import com.vama.eiliyaabedini.common.openUrl
import com.vama.eiliyaabedini.databinding.ActivityAlbumDetailBinding
import com.vama.eiliyaabedini.presentation.adapter.GenreAdapter
import com.vama.eiliyaabedini.presentation.model.State
import com.vama.eiliyaabedini.presentation.viewmodel.AlbumDetailAction
import com.vama.eiliyaabedini.presentation.viewmodel.AlbumDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumDetailActivity : AppCompatActivity() {

    private val genreAdapter: GenreAdapter by lazy { GenreAdapter() }

    private val albumDetailViewModel: AlbumDetailViewModel by viewModel()

    private val binding: ActivityAlbumDetailBinding by lazy { ActivityAlbumDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = genreAdapter

        albumDetailViewModel.data.observe(this) { state ->
            when (state) {
                is State.DataState -> {
                    with(binding) {
                        state.data.let { album ->
                            albumImage.loadImage(album.image)
                            albumNameTextView.text = album.name
                            artistNameTextView.text = album.artist
                            releasedDateTextView.text = resources.getString(R.string.released, album.releaseDate)
                            copyrightTextView.text = album.copyright

                            genreAdapter.submitList(album.genres)

                            visitAlbumButton.setOnClickListener {
                                albumDetailViewModel.onVisitButtonClick(album)
                            }
                        }
                    }
                }

                is State.ErrorState -> {
                    Toast.makeText(this, state.error, Toast.LENGTH_LONG).show()
                    finish()
                }

                is State.LoadingState -> Unit
                is State.Events -> handleEvents(state.action)
            }
        }

        setListeners()

        albumDetailViewModel.onViewCreated(intent.getStringExtra(ALBUM_ID))
    }

    private fun handleEvents(action: AlbumDetailAction) {
        when (action) {
            is AlbumDetailAction.NavigateToUrl -> openUrl(action.url)

            is AlbumDetailAction.CloseActivity -> finish()
        }
    }

    private fun setListeners() {
        genreAdapter.setOnItemClick { genre ->
            albumDetailViewModel.onGenreClick(genre)
        }

        binding.backButton.setOnClickListener {
            albumDetailViewModel.onBackClick()
        }
    }

    companion object {

        private const val ALBUM_ID = "ALBUM_ID"

        fun launch(context: Context, albumId: String) {
            context.startActivity(Intent(context, AlbumDetailActivity::class.java).apply {
                putExtra(ALBUM_ID, albumId)
            })
        }
    }
}
