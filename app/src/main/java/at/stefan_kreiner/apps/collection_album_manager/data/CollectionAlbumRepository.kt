/*
 * Copyright (C) 2023 Stefan Kreiner
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.stefan_kreiner.apps.collection_album_manager.data

import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.LocalCollectionAlbumEntryDao
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.LocalCollectionAlbumEntryWithItemEntries
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema.LocalCollectionAlbumEntry
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema.LocalCollectionAlbumIdentifierType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

typealias CollectionAlbumIdentifierType = LocalCollectionAlbumIdentifierType

data class PreCollectionAlbum(
    val name: String,
    val description: String,
    val itemCount: UInt,
) {
    fun toLocalCollectionAlbumEntry() : LocalCollectionAlbumEntry {
        return LocalCollectionAlbumEntry(
            name = name,
            description = description,
            itemCount = itemCount.toInt(),
        )
    }
}

class CollectionAlbum(
    val identifier: CollectionAlbumIdentifierType,
    val name: String,
    val description: String,
    val itemCount: UInt,
    val items: Set<Int>,
) {
    companion object {
        fun fromLocalCollectionAlbumEntryWithItemEntries(localCollectionAlbumEntryWithItemEntries: LocalCollectionAlbumEntryWithItemEntries): CollectionAlbum {
            return CollectionAlbum(
                identifier = localCollectionAlbumEntryWithItemEntries.albumEntry.identifier,
                name = localCollectionAlbumEntryWithItemEntries.albumEntry.name,
                description = localCollectionAlbumEntryWithItemEntries.albumEntry.description,
                itemCount = localCollectionAlbumEntryWithItemEntries.albumEntry.itemCount.toUInt(),
                items = localCollectionAlbumEntryWithItemEntries.albumItemEntries.map { itemEntry ->
                    itemEntry.itemIndex
                }.toSet()
            )
        }
    }

    fun toLocalCollectionAlbumEntryWithItemEntries(): LocalCollectionAlbumEntryWithItemEntries {
        return LocalCollectionAlbumEntryWithItemEntries(
            albumEntry = LocalCollectionAlbumEntry(
                identifier = identifier,
                name = name,
                description = description,
                itemCount = itemCount.toInt(),
            ),
            albumItemEntries = items.map {
                CollectionAlbumItem(
                    collectionAlbum = this@CollectionAlbum,
                    itemIndex = it,
                ).toLocalCollectionAlbumItemEntry()
            }
        )
    }
}

interface CollectionAlbumRepository {
    val collectionAlbumList: Flow<List<CollectionAlbum>>

    fun getByIdentifierWithItems(collectionAlbumIdentifier: CollectionAlbumIdentifierType): Flow<CollectionAlbum?>
    fun getByNameWithItems(collectionAlbumName: String): Flow<CollectionAlbum?>
    suspend fun insert(preCollectionAlbum: PreCollectionAlbum) : Long
    suspend fun insert(vararg preCollectionAlbums: PreCollectionAlbum) : List<Long>
//    suspend fun update(vararg collectionAlbums: CollectionAlbum)
    suspend fun delete(vararg collectionAlbums: CollectionAlbum)
}

class ProductionCollectionAlbumRepository @Inject constructor(
    private val localCollectionAlbumEntryDao: LocalCollectionAlbumEntryDao,
) : CollectionAlbumRepository {

    override val collectionAlbumList: Flow<List<CollectionAlbum>> =
        localCollectionAlbumEntryDao.getAllWithItemEntries().map { entries ->
            entries.map {
                CollectionAlbum.fromLocalCollectionAlbumEntryWithItemEntries(it)
            }
        }

    override fun getByIdentifierWithItems(collectionAlbumIdentifier: CollectionAlbumIdentifierType): Flow<CollectionAlbum?> {
        return localCollectionAlbumEntryDao.getByIdentifierWithItemEntries(collectionAlbumIdentifier).map {
            if (it == null) {
                null
            } else {
                CollectionAlbum.fromLocalCollectionAlbumEntryWithItemEntries(it)
            }
        }
    }

    override fun getByNameWithItems(collectionAlbumName: String): Flow<CollectionAlbum?> {
        return localCollectionAlbumEntryDao.getByNameWithItemEntries(collectionAlbumName).map {
            if (it == null) {
                null
            } else {
                CollectionAlbum.fromLocalCollectionAlbumEntryWithItemEntries(it)
            }
        }
    }

    override suspend fun insert(preCollectionAlbum: PreCollectionAlbum) : Long {
        return insert(*listOf(preCollectionAlbum).toTypedArray())[0]
    }

    override suspend fun insert(vararg preCollectionAlbums: PreCollectionAlbum) : List<Long> {
        return localCollectionAlbumEntryDao.insert(*preCollectionAlbums.map {
            it.toLocalCollectionAlbumEntry()
        }.toTypedArray())
    }

//    override suspend fun update(vararg collectionAlbums: CollectionAlbum) {
//        localCollectionAlbumEntryDao.insert(*collectionAlbums.map {
//            CollectionAlbum.toLocalCollectionAlbumEntry(it)
//        }.toTypedArray())
//    }


    override suspend fun delete(vararg collectionAlbums: CollectionAlbum) {
        return localCollectionAlbumEntryDao.delete(*collectionAlbums.map {
            it.toLocalCollectionAlbumEntryWithItemEntries()
        }.toTypedArray())
    }
}
