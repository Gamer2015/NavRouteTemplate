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

import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema.LocalCollectionAlbumEntry
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema.LocalCollectionAlbumIdentifierType
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema.LocalCollectionAlbumItemEntry
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class LocalCollectionAlbumEntryWithItemEntries(
    @Embedded val albumEntry: LocalCollectionAlbumEntry,
    @Relation(
        parentColumn = "identifier", entityColumn = "collection_album_identifier"
    ) val albumItemEntries: List<LocalCollectionAlbumItemEntry>,
)

@Dao
abstract class LocalCollectionAlbumEntryDao {
    @Query("SELECT * FROM collection_album ORDER BY name ASC")
    abstract fun getAllWithItemEntries(): Flow<List<LocalCollectionAlbumEntryWithItemEntries>>

    @Transaction
    @Query("SELECT * FROM collection_album WHERE identifier = :collectionAlbumIdentifier")
    abstract fun getByIdentifierWithItemEntries(collectionAlbumIdentifier: LocalCollectionAlbumIdentifierType): Flow<LocalCollectionAlbumEntryWithItemEntries?>

    @Transaction
    @Query("SELECT * FROM collection_album WHERE name = :collectionAlbumName")
    abstract fun getByNameWithItemEntries(collectionAlbumName: String): Flow<LocalCollectionAlbumEntryWithItemEntries?>

    @Insert
    abstract suspend fun insert(vararg localCollectionAlbumEntries: LocalCollectionAlbumEntry): List<Long>

    @Transaction
    open suspend fun delete(vararg localCollectionAlbumEntryWithItemEntries: LocalCollectionAlbumEntryWithItemEntries) {
        delete(*localCollectionAlbumEntryWithItemEntries.map { it.albumEntry }.toTypedArray())
        delete(*localCollectionAlbumEntryWithItemEntries.map { it.albumItemEntries}.flatten().toTypedArray())
    }

    @Delete
    protected abstract suspend fun delete(vararg localCollectionAlbumEntries: LocalCollectionAlbumEntry)

    @Delete
    protected abstract suspend fun delete(vararg localCollectionAlbumItemEntries: LocalCollectionAlbumItemEntry)

//    suspend fun insert(localCollectionAlbumEntry: LocalCollectionAlbumEntry): Long {
//        return insert(*listOf(localCollectionAlbumEntry).toTypedArray())[0]
//    }
//
//    @Transaction
//    open suspend fun insert(vararg localCollectionAlbumEntries: LocalCollectionAlbumEntry): List<Long> {
//        val localCollectionAlbumIdentifiers = forceInsert(*localCollectionAlbumEntries)
//
//        val newCollectionAlbumItems = localCollectionAlbumEntries.indices.map { index ->
//            val localCollectionAlbumIdentifier = localCollectionAlbumIdentifiers[index]
//            (0 until localCollectionAlbumEntries[index].itemCount).map { itemIndex ->
//                LocalCollectionAlbumItemEntry(
//                    collectionAlbumIdentifier = localCollectionAlbumIdentifier,
//                    itemIndex = itemIndex,
//                    collected = false,
//                    itemLabel = (itemIndex + 1).toString()
//                )
//            }
//        }.flatten()
//        forceInsertItems(*newCollectionAlbumItems.toTypedArray())
//
//        return localCollectionAlbumIdentifiers
//    }

//    @Update
//    suspend fun update(vararg localCollectionAlbumEntries: LocalCollectionAlbumEntry)
//
//    @Delete
//    suspend fun delete(vararg localCollectionAlbumEntries: LocalCollectionAlbumEntry)
}