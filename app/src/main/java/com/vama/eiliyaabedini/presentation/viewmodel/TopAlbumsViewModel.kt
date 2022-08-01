package com.vama.eiliyaabedini.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.AppException
import com.vama.eiliyaabedini.domain.repository.MusicAlbumsRepository
import com.vama.eiliyaabedini.presentation.model.State
import kotlinx.coroutines.launch

typealias TopAlbumsState = State<List<Album>, TopAlbumsAction>

sealed class TopAlbumsAction {
    data class NavigateToAlbumDetail(val albumId: String) : TopAlbumsAction()
}

class TopAlbumsViewModel(private val musicAlbumsRepository: MusicAlbumsRepository) : ViewModel() {

    private val _data: MutableLiveData<TopAlbumsState> = MutableLiveData()

    val data: LiveData<TopAlbumsState>
        get() = _data

    private fun emit(state: TopAlbumsState) {
        _data.postValue(state)
    }

    fun onViewCreated() {
        fetchData()
    }

    private fun fetchData(forceToRefresh: Boolean = false) {
        viewModelScope.launch {
            emit(State.LoadingState())
            try {
                val data = musicAlbumsRepository.fetchData(forceToRefresh)
                emit(State.DataState(data))
            } catch (exception: Exception) {
                if (exception is AppException) {
                    emit(State.ErrorState(exception.message ?: ""))
                } else {
                    emit(State.ErrorState("Something went wrong..."))
                }
            }
        }
    }

    fun onSwipeRefresh() {
        fetchData(forceToRefresh = true)
    }

    fun onRetry() {
        fetchData(forceToRefresh = true)
    }

    fun onMusicAlbumClick(album: Album) {
        emit(State.Events(TopAlbumsAction.NavigateToAlbumDetail(album.id)))
    }
}
