package com.vama.eiliyaabedini.di

import com.vama.eiliyaabedini.data.local.LocalMusicAlbumService
import com.vama.eiliyaabedini.data.local.LocalMusicAlbumServiceImpl
import com.vama.eiliyaabedini.data.local.MusicAlbumDataSource
import com.vama.eiliyaabedini.data.local.MusicAlbumDataSourceImpl
import com.vama.eiliyaabedini.data.local.mapper.LocalMusicAlbumsMapper
import com.vama.eiliyaabedini.data.local.mapper.LocalMusicAlbumsMapperImpl
import com.vama.eiliyaabedini.data.local.model.AlbumEntity
import com.vama.eiliyaabedini.data.local.model.GenreEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {

    factory {
        val config = RealmConfiguration.Builder(schema = setOf(AlbumEntity::class, GenreEntity::class))
            .name("Vama Project")
            .build()

        Realm.open(config)
    }

    factoryOf(::LocalMusicAlbumServiceImpl) { bind<LocalMusicAlbumService>() }

    factoryOf(::LocalMusicAlbumsMapperImpl) { bind<LocalMusicAlbumsMapper>() }

    factoryOf(::MusicAlbumDataSourceImpl) { bind<MusicAlbumDataSource>() }
}
