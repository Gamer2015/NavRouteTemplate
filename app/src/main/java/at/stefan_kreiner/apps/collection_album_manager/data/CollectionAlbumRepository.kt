/*
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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import at.stefan_kreiner.apps.collection_album_manager.data.local.database.CollectionAlbum
import at.stefan_kreiner.apps.collection_album_manager.data.local.database.CollectionAlbumDao
import javax.inject.Inject

interface CollectionAlbumRepository {
    val collectionAlbums: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultCollectionAlbumRepository @Inject constructor(
    private val collectionAlbumDao: CollectionAlbumDao
) : CollectionAlbumRepository {

    override val collectionAlbums: Flow<List<String>> =
        collectionAlbumDao.getCollectionAlbums().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        collectionAlbumDao.insertCollectionAlbum(CollectionAlbum(name = name))
    }
}