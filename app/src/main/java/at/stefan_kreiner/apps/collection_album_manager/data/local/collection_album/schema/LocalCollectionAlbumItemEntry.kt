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

package at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.schema

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "collection_album_item",
    primaryKeys = ["collection_album_identifier", "item_index"]
)
data class LocalCollectionAlbumItemEntry(
    @ColumnInfo(name = "collection_album_identifier") val collectionAlbumIdentifier: LocalCollectionAlbumIdentifierType,
    @ColumnInfo(name = "item_index") val itemIndex: Int,
)