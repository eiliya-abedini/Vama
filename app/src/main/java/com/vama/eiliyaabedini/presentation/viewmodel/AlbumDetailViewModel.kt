package com.vama.eiliyaabedini.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.AppException
import com.vama.eiliyaabedini.domain.model.Genre
import com.vama.eiliyaabedini.domain.repository.MusicAlbumsRepository
import com.vama.eiliyaabedini.presentation.model.State
import kotlinx.coroutines.launch

typealias AlbumDetailState = State<Album, AlbumDetailAction>

sealed class AlbumDetailAction {

    data class NavigateToUrl(val url: String) : AlbumDetailAction()

    object CloseActivity : AlbumDetailAction()
}

const val ALBUM_NOT_FOUND_ERROR = "Album Id is not defined"
const val EXCEPTION_MESSAGE = "Something went wrong..."

class AlbumDetailViewModel(private val musicAlbumsRepository: MusicAlbumsRepository) : ViewModel() {

    private val _data: MutableLiveData<AlbumDetailState> = MutableLiveData()

    val data: LiveData<AlbumDetailState>
        get() = _data

    private fun emit(state: AlbumDetailState) {
        _data.postValue(state)
    }

    fun onViewCreated(albumId: String?) {
        if (albumId == null) {
            emit(State.ErrorState(ALBUM_NOT_FOUND_ERROR))
        } else
            fetchData(albumId)
    }

    private fun fetchData(albumId: String) {
        viewModelScope.launch {
            emit(State.LoadingState())
            try {
                val data = musicAlbumsRepository.getAlbum(albumId)
                emit(State.DataState(data))
            } catch (exception: Exception) {
                if (exception is AppException) {
                    emit(State.ErrorState(exception.message ?: ""))
                } else {
                    emit(State.ErrorState(EXCEPTION_MESSAGE))
                }
            }
        }
    }

    fun onVisitButtonClick(album: Album) {
        emit(State.Events(AlbumDetailAction.NavigateToUrl(album.url)))
    }

    fun onGenreClick(genre: Genre) {
        emit(State.Events(AlbumDetailAction.NavigateToUrl(genre.url)))
    }

    fun onBackClick() {
        emit(State.Events(AlbumDetailAction.CloseActivity))
    }
}
