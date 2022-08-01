package com.vama.eiliyaabedini.data.local

import com.vama.eiliyaabedini.data.local.mapper.LocalMusicAlbumsMapper
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.AppException

interface LocalMusicAlbumService {

    suspend fun getAll(): List<Album>
    suspend fun insertAndReplaceAll(data: List<Album>)
    suspend fun getAlbum(albumId: String): Album
}

class LocalMusicAlbumServiceImpl(
    private val musicAlbumDataSource: MusicAlbumDataSource,
    private val localMusicAlbumsMapper: LocalMusicAlbumsMapper
) : LocalMusicAlbumService {

    override suspend fun getAll(): List<Album> {
        val musicAlbumEntities = musicAlbumDataSource.getAll()
        return localMusicAlbumsMapper.toAlbums(musicAlbumEntities)
    }

    override suspend fun insertAndReplaceAll(data: List<Album>) {
        val entities = localMusicAlbumsMapper.toEntity(data)
        musicAlbumDataSource.insertAndReplaceAll(entities)
    }

    override suspend fun getAlbum(albumId: String): Album {
        val album = musicAlbumDataSource.find(albumId)
        return album?.let {
            localMusicAlbumsMapper.toAlbum(it)
        } ?: throw AppException("Album with id: $albumId not found", null)
    }
}
