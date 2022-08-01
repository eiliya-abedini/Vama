package com.vama.eiliyaabedini.data.remote.model

data class AlbumResponse(
    val id: String,
    val name: String,
    val artistName: String,
    val releaseDate: String,
    val kind: String,
    val artistId: String,
    val artistUrl: String,
    val contentAdvisoryRating: String,
    val artworkUrl100: String,
    val genres: List<GenreResponse>,
    val url: String
)

data class GenreResponse(
    val id: String,
    val name: String,
    val url: String
)
