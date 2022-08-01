package com.vama.eiliyaabedini.di

import com.vama.eiliyaabedini.BuildConfig
import com.vama.eiliyaabedini.data.remote.MusicAlbumsApi
import com.vama.eiliyaabedini.data.remote.RemoteMusicAlbumsService
import com.vama.eiliyaabedini.data.remote.RemoteMusicAlbumsServiceImpl
import com.vama.eiliyaabedini.data.remote.mapper.RemoteMusicAlbumsMapper
import com.vama.eiliyaabedini.data.remote.mapper.RemoteMusicAlbumsMapperImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://rss.applemarketingtools.com/api/v2/"

val networkModule = module {

    factory {
        OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG)
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()
    }

    factory {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory<MusicAlbumsApi> { get<Retrofit>().create(MusicAlbumsApi::class.java) }

    factoryOf(::RemoteMusicAlbumsServiceImpl) { bind<RemoteMusicAlbumsService>() }

    factoryOf(::RemoteMusicAlbumsMapperImpl) { bind<RemoteMusicAlbumsMapper>() }
}
