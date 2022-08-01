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

class TopAlbumsViewModelTest {

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testHelper = TestHelper()

    private val musicAlbumsRepository: MusicAlbumsRepository = mock()

    private val viewModel = TopAlbumsViewModel(musicAlbumsRepository)

    @Test
    fun `when onViewCreated should fetch data from repository with forceToRefresh as false`() {
        coroutineScope.runBlockingTest {
            viewModel.onViewCreated()

            verify(musicAlbumsRepository).fetchData(false)
        }
    }

    @Test
    fun `when onViewCreated called then state must be LoadingState`() {
        val states = LinkedList<TopAlbumsState>()
        val observer = Observer<TopAlbumsState> { states.add(it) }

        viewModel.data.observeForever(observer)

        try {
            viewModel.onViewCreated()

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
            given(musicAlbumsRepository.fetchData(any())).willAnswer {
                throw AppException(errorMessage, Throwable(errorMessage))
            }

            viewModel.onViewCreated()

            assertEquals(
                viewModel.data.value!!.javaClass,
                State.ErrorState<Album, TopAlbumsAction>("").javaClass
            )
        }
    }

    @Test
    fun `when onViewCreated called and fetchData return value then should return DataState`() {
        coroutineScope.runBlockingTest {
            whenever(musicAlbumsRepository.fetchData(any())) doReturn testHelper.albums

            val states = LinkedList<TopAlbumsState>()
            val observer = Observer<TopAlbumsState> { states.add(it) }
            viewModel.onViewCreated()

            try {
                viewModel.data.observeForever(observer)
                assertEquals(
                    (states.last as State.DataState).data,
                    testHelper.albums
                )
            } finally {
                viewModel.data.removeObserver(observer)
            }
        }
    }

    @Test
    fun `when onSwipeRefresh should fetchData with forceToRefresh as true`() {
        coroutineScope.runBlockingTest {
            viewModel.onSwipeRefresh()

            verify(musicAlbumsRepository).fetchData(true)
        }
    }

    @Test
    fun `when onRetry should fetchData with forceToRefresh as true`() {
        coroutineScope.runBlockingTest {
            viewModel.onRetry()

            verify(musicAlbumsRepository).fetchData(true)
        }
    }

    @Test
    fun `when onMusicAlbumClick should emit navigation to album detail event with its id`() {
        val states = LinkedList<TopAlbumsState>()
        val observer = Observer<TopAlbumsState> { states.add(it) }

        viewModel.onMusicAlbumClick(testHelper.album)

        try {
            viewModel.data.observeForever(observer)
            assertEquals(
                (states.last as State.Events).action,
                TopAlbumsAction.NavigateToAlbumDetail(testHelper.album.id)
            )
        } finally {
            viewModel.data.removeObserver(observer)
        }
    }
}
