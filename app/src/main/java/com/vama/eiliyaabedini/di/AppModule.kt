package com.vama.eiliyaabedini.di

import com.vama.eiliyaabedini.domain.repository.MusicAlbumsRepository
import com.vama.eiliyaabedini.domain.repository.MusicAlbumsRepositoryImpl
import com.vama.eiliyaabedini.presentation.viewmodel.AlbumDetailViewModel
import com.vama.eiliyaabedini.presentation.viewmodel.TopAlbumsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {

    factoryOf(::MusicAlbumsRepositoryImpl) { bind<MusicAlbumsRepository>() }

    viewModelOf(::TopAlbumsViewModel)

    viewModelOf(::AlbumDetailViewModel)
}
