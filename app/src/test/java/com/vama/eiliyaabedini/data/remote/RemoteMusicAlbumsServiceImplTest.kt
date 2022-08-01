package com.vama.eiliyaabedini.data.remote

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vama.eiliyaabedini.data.TestHelper
import com.vama.eiliyaabedini.data.remote.mapper.RemoteMusicAlbumsMapper
import com.vama.eiliyaabedini.domain.model.AppException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.io.IOException

const val TOTAL_ITEMS = 100

class RemoteMusicAlbumsServiceImplTest {

    private val testHelper = TestHelper()

    private val mockMusicAlbumsApi: MusicAlbumsApi = mock()

    private val mockRemoteMusicAlbumsMapper: RemoteMusicAlbumsMapper = mock()

    private val remoteMusicAlbumsService = RemoteMusicAlbumsServiceImpl(mockMusicAlbumsApi, mockRemoteMusicAlbumsMapper)

    @Test
    fun `when fetchTopPlayed called and result is successful should map and return data`() {
        runBlocking {
            whenever(mockMusicAlbumsApi.fetchTopPlayed(TOTAL_ITEMS)) doReturn Response.success(testHelper.serverResponse)
            whenever(mockRemoteMusicAlbumsMapper.toAlbums(any(), any())) doReturn testHelper.albums

            val response = remoteMusicAlbumsService.fetchTopPlayed()

            verify(mockMusicAlbumsApi).fetchTopPlayed(any())
            verify(mockRemoteMusicAlbumsMapper).toAlbums(any(), any())
            assertEquals(testHelper.albums, response)
        }
    }

    @Test(expected = AppException::class)
    fun `when fetchTopPlayed called and result is not successful should throw appException`() {
        runBlocking {
            whenever(mockMusicAlbumsApi.fetchTopPlayed(TOTAL_ITEMS)) doReturn Response.error(
                500,
                byteArrayOf().toResponseBody()
            )

            remoteMusicAlbumsService.fetchTopPlayed()
        }
    }

    @Test(expected = AppException::class)
    fun `when fetchTopPlayed called and request return exception should throw appException`() {
        runBlocking {
            given(mockMusicAlbumsApi.fetchTopPlayed(TOTAL_ITEMS)).willAnswer { throw IOException() }

            remoteMusicAlbumsService.fetchTopPlayed()
        }
    }
}
