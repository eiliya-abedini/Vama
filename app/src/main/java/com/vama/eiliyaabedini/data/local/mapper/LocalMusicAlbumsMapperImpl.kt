package com.vama.eiliyaabedini.data.local.mapper

import com.vama.eiliyaabedini.data.local.model.AlbumEntity
import com.vama.eiliyaabedini.data.local.model.GenreEntity
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.Genre
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList

interface LocalMusicAlbumsMapper {

    fun toAlbums(albumEntities: List<AlbumEntity>): List<Album>
    fun toAlbum(albumEntity: AlbumEntity): Album
    fun toEntity(albums: List<Album>): List<AlbumEntity>
}

class LocalMusicAlbumsMapperImpl : LocalMusicAlbumsMapper {

    override fun toAlbums(albumEntities: List<AlbumEntity>): List<Album> {
        return albumEntities.map { albumEntity ->
            toAlbum(albumEntity)
        }
    }

    override fun toAlbum(albumEntity: AlbumEntity) = Album(
        id = albumEntity.id,
        name = albumEntity.name,
        artist = albumEntity.artist,
        thumbnail = albumEntity.thumbnail,
        image = albumEntity.image,
        genres = albumEntity.genres.map { it.toGenre() },
        url = albumEntity.url,
        releaseDate = albumEntity.releaseDate,
        copyright = albumEntity.copyright
    )

    override fun toEntity(albums: List<Album>): List<AlbumEntity> {
        return albums.map { album ->
            AlbumEntity().apply {
                id = album.id
                name = album.name
                artist = album.artist
                thumbnail = album.thumbnail
                image = album.image
                genres = album.genres.toGenreEntities()
                url = album.url
                releaseDate = album.releaseDate
                copyright = album.copyright
            }
        }
    }

    private fun GenreEntity.toGenre() = Genre(
        name = name,
        url = url
    )

    private fun List<Genre>.toGenreEntities(): RealmList<GenreEntity> {
        val entities = map {
            GenreEntity().apply {
                name = it.name
                url = it.url
            }
        }
        return realmListOf(*entities.toTypedArray())
    }
}
