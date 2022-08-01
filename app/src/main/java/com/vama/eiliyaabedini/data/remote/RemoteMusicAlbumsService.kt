package com.vama.eiliyaabedini.data.remote

import com.vama.eiliyaabedini.data.remote.mapper.RemoteMusicAlbumsMapper
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.AppException

const val TOTAL_ITEMS = 100
const val ERROR_MESSAGE = "Problem while fetching data"

interface RemoteMusicAlbumsService {

    @Throws(AppException::class)
    suspend fun fetchTopPlayed(): List<Album>
}

class RemoteMusicAlbumsServiceImpl(
    private val musicAlbumsApi: MusicAlbumsApi,
    private val remoteMusicAlbumsMapper: RemoteMusicAlbumsMapper
) : RemoteMusicAlbumsService {

    override suspend fun fetchTopPlayed(): List<Album> {
        try {
            val response = musicAlbumsApi.fetchTopPlayed(count = TOTAL_ITEMS)
            val body = response.body()
            return if (response.isSuccessful && body != null) {
                remoteMusicAlbumsMapper.toAlbums(body.feed.copyright, body.feed.results)
            } else {
                throw AppException(ERROR_MESSAGE, null)
            }
        } catch (exception: Exception) {
            throw AppException(ERROR_MESSAGE, exception)
        }
    }
}
