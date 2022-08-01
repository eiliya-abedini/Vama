package com.vama.eiliyaabedini.data.local.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class AlbumEntity : RealmObject {

    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var artist: String = ""
    var thumbnail: String = ""
    var image: String = ""
    var url: String = ""
    var releaseDate: String = ""
    var copyright: String = ""
    var genres: RealmList<GenreEntity> = realmListOf()
}

class GenreEntity : RealmObject {

    var name: String = ""
    var url: String = ""
}
