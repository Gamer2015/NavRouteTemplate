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

package at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album

import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema.LocalCollectionAlbumItemEntry
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema.LocalCollectionAlbumEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalCollectionAlbumItemEntryDao {
    @Transaction
    suspend fun CUD(
        toInsert: List<LocalCollectionAlbumItemEntry>,
        toUpdate: List<LocalCollectionAlbumItemEntry>,
        toDelete: List<LocalCollectionAlbumItemEntry>,
    ) {
        insert(*toInsert.toTypedArray())
        update(*toUpdate.toTypedArray())
        delete(*toDelete.toTypedArray())
    }

    @Insert
    suspend fun insert(vararg localCollectionAlbumItemEntries: LocalCollectionAlbumItemEntry)

    @Update
    suspend fun update(vararg localCollectionAlbumItemEntries: LocalCollectionAlbumItemEntry)

    @Delete
    suspend fun delete(vararg localCollectionAlbumItemEntries: LocalCollectionAlbumItemEntry)
}
