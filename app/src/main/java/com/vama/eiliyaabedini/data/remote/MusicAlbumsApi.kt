package com.vama.eiliyaabedini.data.remote

import com.vama.eiliyaabedini.data.remote.model.AlbumResponse
import com.vama.eiliyaabedini.data.remote.model.ServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicAlbumsApi {

    @GET("us/music/most-played/{count}/albums.json")
    suspend fun fetchTopPlayed(@Path("count") count: Int): Response<ServerResponse<AlbumResponse>>
}
