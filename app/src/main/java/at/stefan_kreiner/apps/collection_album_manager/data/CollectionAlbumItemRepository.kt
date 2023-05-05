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

import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.LocalCollectionAlbumItemEntryDao
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema.LocalCollectionAlbumItemEntry
import javax.inject.Inject

class CollectionAlbumItem(
    val collectionAlbum: CollectionAlbum,
    val itemIndex: Int,
) {
    fun toLocalCollectionAlbumItemEntry(): LocalCollectionAlbumItemEntry {
        return LocalCollectionAlbumItemEntry(
            collectionAlbumIdentifier = collectionAlbum.identifier,
            itemIndex = itemIndex,
        )
    }
}

interface CollectionAlbumItemRepository {
    suspend fun CUD(
        toInsert: List<CollectionAlbumItem>,
        toUpdate: List<CollectionAlbumItem>,
        toDelete: List<CollectionAlbumItem>,
    )
}

class ProductionCollectionAlbumItemRepository @Inject constructor(
    private val localCollectionAlbumItemEntryDao: LocalCollectionAlbumItemEntryDao,
) : CollectionAlbumItemRepository {
    override suspend fun CUD(
        toInsert: List<CollectionAlbumItem>,
        toUpdate: List<CollectionAlbumItem>,
        toDelete: List<CollectionAlbumItem>,
    ) {
        localCollectionAlbumItemEntryDao.CUD(toInsert = toInsert.map {
            it.toLocalCollectionAlbumItemEntry()
        }, toUpdate = toUpdate.map {
            it.toLocalCollectionAlbumItemEntry()
        }, toDelete = toDelete.map {
            it.toLocalCollectionAlbumItemEntry()
        })
    }
}
