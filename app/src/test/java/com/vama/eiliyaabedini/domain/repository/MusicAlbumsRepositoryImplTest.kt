package com.vama.eiliyaabedini.domain.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vama.eiliyaabedini.data.TestHelper
import com.vama.eiliyaabedini.data.local.LocalMusicAlbumService
import com.vama.eiliyaabedini.data.remote.RemoteMusicAlbumsService
import com.vama.eiliyaabedini.domain.model.AppException
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MusicAlbumsRepositoryImplTest {

    private val testHelper = TestHelper()

    private val remoteMusicAlbumsService: RemoteMusicAlbumsService = mock()

    private val localMusicAlbumsService: LocalMusicAlbumService = mock()

    private val musicAlbumsRepositoryImpl = MusicAlbumsRepositoryImpl(remoteMusicAlbumsService, localMusicAlbumsService)

    @Test
    fun `when fetchData with forceToRefresh should fetch data from remote service and update local storage`() {
        runBlocking {
            whenever(remoteMusicAlbumsService.fetchTopPlayed()) doReturn testHelper.albums

            musicAlbumsRepositoryImpl.fetchData(forceToRefresh = true)

            verify(remoteMusicAlbumsService).fetchTopPlayed()
            verify(localMusicAlbumsService).insertAndReplaceAll(any())
            verify(localMusicAlbumsService, never()).getAll()
        }
    }

    @Test
    fun `when fetchData with forceToRefresh but with server error should fetch data from local storage`() {
        runBlocking {
            given(remoteMusicAlbumsService.fetchTopPlayed()).willAnswer { throw AppException("ConnectionError", null) }
            whenever(localMusicAlbumsService.getAll()) doReturn testHelper.albums

            musicAlbumsRepositoryImpl.fetchData(forceToRefresh = true)

            verify(remoteMusicAlbumsService).fetchTopPlayed()
            verify(localMusicAlbumsService, never()).insertAndReplaceAll(any())
            verify(localMusicAlbumsService).getAll()
        }
    }

    @Test(expected = AppException::class)
    fun `when fetchData with forceToRefresh but with server error and not data should throw AppException`() {
        runBlocking {
            given(remoteMusicAlbumsService.fetchTopPlayed()).willAnswer { throw AppException("ConnectionError", null) }
            whenever(localMusicAlbumsService.getAll()) doReturn emptyList()

            musicAlbumsRepositoryImpl.fetchData(forceToRefresh = true)
        }
    }

    @Test
    fun `when fetchData without forceToRefresh should fetch data from local storage`() {
        runBlocking {
            whenever(localMusicAlbumsService.getAll()) doReturn testHelper.albums

            musicAlbumsRepositoryImpl.fetchData(forceToRefresh = false)

            verify(localMusicAlbumsService).getAll()
            verify(remoteMusicAlbumsService, never()).fetchTopPlayed()
            verify(localMusicAlbumsService, never()).insertAndReplaceAll(any())
        }
    }

    @Test
    fun `when fetchData without forceToRefresh but local storage is empty should fetch data from remote service`() {
        runBlocking {
            whenever(localMusicAlbumsService.getAll()) doReturn emptyList()

            musicAlbumsRepositoryImpl.fetchData(forceToRefresh = false)

            verify(localMusicAlbumsService).getAll()
            verify(remoteMusicAlbumsService).fetchTopPlayed()
        }
    }
}
