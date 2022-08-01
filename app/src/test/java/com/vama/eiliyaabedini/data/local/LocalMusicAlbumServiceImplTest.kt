package com.vama.eiliyaabedini.data.local

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vama.eiliyaabedini.data.TestHelper
import com.vama.eiliyaabedini.data.local.mapper.LocalMusicAlbumsMapper
import com.vama.eiliyaabedini.domain.model.AppException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalMusicAlbumServiceImplTest {

    private val testHelper = TestHelper()

    private val mockLocalMusicAlbumsMapper: LocalMusicAlbumsMapper = mock()

    private val mockMusicAlbumDataSource: MusicAlbumDataSource = mock()

    private val localMusicAlbumService = LocalMusicAlbumServiceImpl(mockMusicAlbumDataSource, mockLocalMusicAlbumsMapper)

    @Test
    fun `when getAll called should getAll data from data source and map data`() {
        runBlocking {
            whenever(mockMusicAlbumDataSource.getAll()) doReturn testHelper.albumEntities
            whenever(mockLocalMusicAlbumsMapper.toAlbums(any())) doReturn testHelper.albums

            val response = localMusicAlbumService.getAll()

            verify(mockMusicAlbumDataSource).getAll()
            verify(mockLocalMusicAlbumsMapper).toAlbums(any())
            assertEquals(testHelper.albums, response)
        }
    }

    @Test
    fun `when insertAndReplaceAll called should map data to entity and call insertAndReplaceAll of data source`() {
        runBlocking {
            whenever(mockLocalMusicAlbumsMapper.toEntity(any())) doReturn testHelper.albumEntities

            localMusicAlbumService.insertAndReplaceAll(testHelper.albums)

            verify(mockLocalMusicAlbumsMapper).toEntity(any())
            verify(mockMusicAlbumDataSource).insertAndReplaceAll(any())
        }
    }

    @Test
    fun `when getAlbum called should return get and return mapped album`() {
        runBlocking {
            whenever(mockMusicAlbumDataSource.find(any())) doReturn testHelper.albumEntity
            whenever(mockLocalMusicAlbumsMapper.toAlbum(any())) doReturn testHelper.album

            localMusicAlbumService.getAlbum("1234")

            verify(mockMusicAlbumDataSource).find("1234")
            verify(mockLocalMusicAlbumsMapper).toAlbum(any())
        }
    }

    @Test(expected = AppException::class)
    fun `when getAlbum called and album not found should return AppException`() {
        runBlocking {
            whenever(mockMusicAlbumDataSource.find("1234")) doReturn null

            localMusicAlbumService.getAlbum("1234")

            verify(mockMusicAlbumDataSource).find("1234")
            verify(mockLocalMusicAlbumsMapper).toAlbum(any())
        }
    }
}
