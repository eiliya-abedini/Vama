package com.vama.eiliyaabedini.data.local.mapper

import com.vama.eiliyaabedini.data.TestHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalMusicAlbumsMapperImplTest {

    private val testHelper = TestHelper()

    private val localMusicAlbumsMapperImpl = LocalMusicAlbumsMapperImpl()

    @Test
    fun `when toAlbum called with entity data should return album data`() {
        val result = localMusicAlbumsMapperImpl.toAlbum(testHelper.albumEntity)

        assertEquals(testHelper.album, result)
    }

    @Test
    fun `when toAlbums called with entity data should return albums data`() {
        val result = localMusicAlbumsMapperImpl.toAlbums(testHelper.albumEntities)

        assertEquals(testHelper.albums, result)
    }
}
