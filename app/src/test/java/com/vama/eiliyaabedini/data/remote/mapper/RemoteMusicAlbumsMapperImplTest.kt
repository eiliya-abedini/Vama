package com.vama.eiliyaabedini.data.remote.mapper

import com.vama.eiliyaabedini.data.TestHelper
import org.junit.Assert
import org.junit.Test

class RemoteMusicAlbumsMapperImplTest {

    private val testHelper = TestHelper()

    private val remoteMusicAlbumsMapperImpl = RemoteMusicAlbumsMapperImpl()

    @Test
    fun `when toAlbum called with entity data should return album data`() {
        val result = remoteMusicAlbumsMapperImpl.toAlbums("Apple inc", testHelper.albumResponses)

        Assert.assertEquals(testHelper.albums, result)
    }
}
