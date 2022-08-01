package com.vama.eiliyaabedini.data

import com.vama.eiliyaabedini.data.local.model.AlbumEntity
import com.vama.eiliyaabedini.data.local.model.GenreEntity
import com.vama.eiliyaabedini.data.remote.model.AlbumResponse
import com.vama.eiliyaabedini.data.remote.model.GenreResponse
import com.vama.eiliyaabedini.data.remote.model.ServerFeed
import com.vama.eiliyaabedini.data.remote.model.ServerResponse
import com.vama.eiliyaabedini.domain.model.Album
import com.vama.eiliyaabedini.domain.model.Genre
import io.realm.kotlin.ext.realmListOf

class TestHelper {

    val albumResponse1 = AlbumResponse(
        id = "12",
        name = "FirstAlbum",
        artistName = "firstArtist",
        releaseDate = "25/04/2022",
        kind = "kind",
        artistId = "232",
        contentAdvisoryRating = "2.2",
        artistUrl = "http://artistUrl.com",
        artworkUrl100 = "http://albumImage.com/100x100bb.jpg",
        genres = listOf(
            GenreResponse("1", "Rap", "http://rap.com"),
            GenreResponse("2", "Pop", "http://pop.com"),
        ),
        url = "http://arlbumURL.com"
    )

    val albumResponse2 = AlbumResponse(
        id = "13",
        name = "SecondAlbum",
        artistName = "secondArtist",
        releaseDate = "25/02/2022",
        kind = "kind",
        artistId = "2322",
        contentAdvisoryRating = "4.2",
        artistUrl = "http://artistUrl2.com",
        artworkUrl100 = "http://albumImage.com/some-more-info/100x100bb.jpg",
        genres = listOf(
            GenreResponse("3", "Rock", "http://rock.com")
        ),
        url = "http://arlbumURL2.com"
    )

    val albumResponses = listOf(albumResponse1, albumResponse2)

    val serverResponse = ServerResponse(
        ServerFeed(
            id = "123123",
            title = "App feed",
            copyright = "Apple inc",
            country = "USA",
            icon = "someIconUrl",
            updated = "23",
            results = albumResponses
        )
    )

    val rapGenre = Genre("Rap", "http://rap.com")

    val album = Album(
        id = "12",
        name = "FirstAlbum",
        artist = "firstArtist",
        releaseDate = "25/04/2022",
        thumbnail = "http://albumImage.com/100x100bb.jpg",
        image = "http://albumImage.com/800x800bb.jpg",
        genres = listOf(
            rapGenre,
            Genre("Pop", "http://pop.com"),
        ),
        url = "http://arlbumURL.com",
        copyright = "Apple inc"
    )

    val albums: List<Album> = listOf(
        album,
        Album(
            id = "13",
            name = "SecondAlbum",
            artist = "secondArtist",
            releaseDate = "25/02/2022",
            thumbnail = "http://albumImage.com/some-more-info/100x100bb.jpg",
            image = "http://albumImage.com/some-more-info/800x800bb.jpg",
            genres = listOf(
                Genre("Rock", "http://rock.com"),
            ),
            url = "http://arlbumURL2.com",
            copyright = "Apple inc"
        )
    )

    val albumEntity = AlbumEntity().apply {
        id = "12"
        name = "FirstAlbum"
        artist = "firstArtist"
        releaseDate = "25/04/2022"
        thumbnail = "http://albumImage.com/100x100bb.jpg"
        image = "http://albumImage.com/800x800bb.jpg"
        genres = realmListOf(
            GenreEntity().apply {
                name = "Rap"
                url = "http://rap.com"
            },
            GenreEntity().apply {
                name = "Pop"
                url = "http://pop.com"
            })
        url = "http://arlbumURL.com"
        copyright = "Apple inc"
    }

    val albumEntities: List<AlbumEntity> = listOf(
        albumEntity,
        AlbumEntity().apply {
            id = "13"
            name = "SecondAlbum"
            artist = "secondArtist"
            releaseDate = "25/02/2022"
            thumbnail = "http://albumImage.com/some-more-info/100x100bb.jpg"
            image = "http://albumImage.com/some-more-info/800x800bb.jpg"
            genres = realmListOf(
                GenreEntity().apply {
                    name = "Rock"
                    url = "http://rock.com"
                }
            )
            url = "http://arlbumURL2.com"
            copyright = "Apple inc"
        }
    )
}
