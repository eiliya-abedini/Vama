package com.vama.eiliyaabedini.domain.repository

import com.vama.eiliyaabedini.data.local.LocalMusicAlbumService
import com.vama.eiliyaabedini.data.remote.RemoteMusicAlbumsService
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.AppException

interface MusicAlbumsRepository {

    suspend fun fetchData(forceToRefresh: Boolean): List<Album>
    suspend fun getAlbum(albumId: String): Album
}

class MusicAlbumsRepositoryImpl(
    private val remoteMusicAlbumsService: RemoteMusicAlbumsService,
    private val localMusicAlbumsService: LocalMusicAlbumService
) : MusicAlbumsRepository {

    override suspend fun fetchData(forceToRefresh: Boolean): List<Album> {
        return if (forceToRefresh) {
            fetchAndUpdateDataFromRemoteServer()
        } else {
            fetchDataFromLocalStorage().ifEmpty {
                fetchAndUpdateDataFromRemoteServer()
            }
        }
    }

    override suspend fun getAlbum(albumId: String): Album = localMusicAlbumsService.getAlbum(albumId)

    private suspend fun fetchDataFromLocalStorage(): List<Album> = localMusicAlbumsService.getAll()

    private suspend fun fetchAndUpdateDataFromRemoteServer(): List<Album> {
        return try {
            remoteMusicAlbumsService.fetchTopPlayed().also {
                localMusicAlbumsService.insertAndReplaceAll(it)
            }
        } catch (exception: AppException) {
            fetchDataFromLocalStorage().ifEmpty {
                throw exception
            }
        }
    }
}
