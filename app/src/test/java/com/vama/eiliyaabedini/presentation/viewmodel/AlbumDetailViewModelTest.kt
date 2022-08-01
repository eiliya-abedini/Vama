package com.vama.eiliyaabedini.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vama.eiliyaabedini.MainCoroutineScopeRule
import com.vama.eiliyaabedini.data.TestHelper
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.AppException
import com.vama.eiliyaabedini.domain.repository.MusicAlbumsRepository
import com.vama.eiliyaabedini.presentation.model.State
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.LinkedList

class AlbumDetailViewModelTest {

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testHelper = TestHelper()

    private val musicAlbumsRepository: MusicAlbumsRepository = mock()

    private val viewModel = AlbumDetailViewModel(musicAlbumsRepository)

    private val albumId = "1234"

    @Test
    fun `when onViewCreated should get album from repository with albumId`() {
        coroutineScope.runBlockingTest {
            viewModel.onViewCreated(albumId)

            verify(musicAlbumsRepository).getAlbum(albumId)
        }
    }

    @Test
    fun `when onViewCreated called then state must be LoadingState`() {
        val states = LinkedList<AlbumDetailState>()
        val observer = Observer<AlbumDetailState> { states.add(it) }
        viewModel.data.observeForever(observer)

        try {
            viewModel.onViewCreated(albumId)

            assertEquals(
                states.first.javaClass,
                State.LoadingState<Album, TopAlbumsAction>().javaClass
            )
        } finally {
            viewModel.data.removeObserver(observer)
        }
    }

    @Test
    fun `when onViewCreated called and fetchData has error should return ErrorState`() {
        coroutineScope.runBlockingTest {
            val errorMessage = "Simple Error"
            given(musicAlbumsRepository.getAlbum(any())).willAnswer {
                throw AppException(errorMessage, Throwable(errorMessage))
            }

            viewModel.onViewCreated(albumId)

            assertEquals(
                viewModel.data.value!!.javaClass,
                State.ErrorState<Album, AlbumDetailAction>(EXCEPTION_MESSAGE).javaClass
            )
        }
    }

    @Test
    fun `when onViewCreated called and fetchData return value then should return DataState`() {
        coroutineScope.runBlockingTest {
            whenever(musicAlbumsRepository.getAlbum(any())) doReturn testHelper.album
            val states = LinkedList<AlbumDetailState>()
            val observer = Observer<AlbumDetailState> { states.add(it) }

            viewModel.onViewCreated(albumId)

            try {
                viewModel.data.observeForever(observer)
                assertEquals((states.last as State.DataState).data, testHelper.album)
            } finally {
                viewModel.data.removeObserver(observer)
            }
        }
    }

    @Test
    fun `when onVisitButtonClick should emit navigation to url event with album url`() {
        val states = LinkedList<AlbumDetailState>()
        val observer = Observer<AlbumDetailState> { states.add(it) }

        viewModel.onVisitButtonClick(testHelper.album)

        try {
            viewModel.data.observeForever(observer)
            assertEquals((states.last as State.Events).action, AlbumDetailAction.NavigateToUrl(testHelper.album.url))
        } finally {
            viewModel.data.removeObserver(observer)
        }
    }

    @Test
    fun `when onGenreClick should emit navigation to url event with genre url`() {
        val states = LinkedList<AlbumDetailState>()
        val observer = Observer<AlbumDetailState> { states.add(it) }

        viewModel.onGenreClick(testHelper.rapGenre)

        try {
            viewModel.data.observeForever(observer)
            assertEquals((states.last as State.Events).action, AlbumDetailAction.NavigateToUrl(testHelper.rapGenre.url))
        } finally {
            viewModel.data.removeObserver(observer)
        }
    }

    @Test
    fun `when onBackClick should emit closeActivity event`() {
        val states = LinkedList<AlbumDetailState>()
        val observer = Observer<AlbumDetailState> { states.add(it) }

        viewModel.onBackClick()

        try {
            viewModel.data.observeForever(observer)
            assertEquals((states.last as State.Events).action, AlbumDetailAction.CloseActivity)
        } finally {
            viewModel.data.removeObserver(observer)
        }
    }
}
