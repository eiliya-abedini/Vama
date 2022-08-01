package com.vama.eiliyaabedini.data.remote.mapper

import com.vama.eiliyaabedini.data.remote.model.AlbumResponse
import com.vama.eiliyaabedini.data.remote.model.GenreResponse
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.Genre

interface RemoteMusicAlbumsMapper {

    fun toAlbums(copyrightInfo: String, albumsResponse: List<AlbumResponse>): List<Album>
}

class RemoteMusicAlbumsMapperImpl : RemoteMusicAlbumsMapper {

    override fun toAlbums(copyrightInfo: String, albumsResponse: List<AlbumResponse>): List<Album> {
        return albumsResponse.map { response ->
            Album(
                id = response.id,
                name = response.name,
                artist = response.artistName,
                thumbnail = response.artworkUrl100,
                image = response.artworkUrl100.convertToBigImage(),
                genres = response.genres.map { it.toGenre() },
                url = response.url,
                releaseDate = response.releaseDate,
                copyright = copyrightInfo
            )
        }
    }

    private fun GenreResponse.toGenre() = Genre(
        name = name,
        url = url
    )
}

private fun String.convertToBigImage() = replace("/100x100bb.jpg", "/800x800bb.jpg")
