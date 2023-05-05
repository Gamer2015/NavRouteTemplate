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

package at.stefan_kreiner.apps.collection_album_manager.data.local.di

import android.content.Context
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.LocalCollectionAlbumDatabase
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.LocalCollectionAlbumEntryDao
import at.stefan_kreiner.apps.collection_album_manager.data.local.collection_album.LocalCollectionAlbumItemEntryDao
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DependencyProvider {
    @Provides
    @Singleton
    fun provideLocalCollectionAlbumDatabase(@ApplicationContext appContext: Context): LocalCollectionAlbumDatabase {
        return Room.databaseBuilder(
            appContext,
            LocalCollectionAlbumDatabase::class.java,
            "LocalCollectionAlbumDatabase"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideLocalCollectionAlbumEntryDao(localCollectionAlbumDatabase: LocalCollectionAlbumDatabase): LocalCollectionAlbumEntryDao {
        return localCollectionAlbumDatabase.localCollectionAlbumDao()
    }

    @Provides
    fun provideLocalCollectionAlbumItemEntryDao(localCollectionAlbumDatabase: LocalCollectionAlbumDatabase): LocalCollectionAlbumItemEntryDao {
        return localCollectionAlbumDatabase.localCollectionAlbumItemDao()
    }
}