package com.vama.eiliyaabedini.data.local

import com.vama.eiliyaabedini.data.local.model.AlbumEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import kotlinx.coroutines.flow.first

interface MusicAlbumDataSource {

    suspend fun getAll(): List<AlbumEntity>
    suspend fun insertAndReplaceAll(entities: List<AlbumEntity>)
    suspend fun find(albumId: String): AlbumEntity?
}

class MusicAlbumDataSourceImpl(
    private val realm: Realm
) : MusicAlbumDataSource {

    override suspend fun getAll(): List<AlbumEntity> {
        return realm.query(AlbumEntity::class).find().asFlow().first().list
    }

    override suspend fun insertAndReplaceAll(entities: List<AlbumEntity>) {
        realm.write {
            val existItems = query(AlbumEntity::class).find()
            delete(existItems)

            entities.forEach { entity ->
                copyToRealm(entity, UpdatePolicy.ALL)
            }
        }
    }

    override suspend fun find(albumId: String): AlbumEntity? {
        return realm.query(AlbumEntity::class, "id == $0", albumId).first().find()?.asFlow()?.first()?.obj
    }
}
