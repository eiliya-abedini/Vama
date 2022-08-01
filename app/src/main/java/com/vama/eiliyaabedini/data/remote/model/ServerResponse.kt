package com.vama.eiliyaabedini.data.remote.model

data class ServerResponse<T>(
    val feed: ServerFeed<T>
)

data class ServerFeed<T>(
    val title: String,
    val id: String,
    val copyright: String,
    val country: String,
    val icon: String,
    val updated: String,
    val results: List<T>,
)
