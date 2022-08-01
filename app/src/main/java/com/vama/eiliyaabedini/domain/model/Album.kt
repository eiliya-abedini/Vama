package com.vama.eiliyaabedini.domain.model

data class Album(
    val id: String,
    val name: String,
    val artist: String,
    val thumbnail: String,
    val image: String,
    val genres: List<Genre>,
    val url: String,
    val releaseDate: String,
    val copyright: String
)
