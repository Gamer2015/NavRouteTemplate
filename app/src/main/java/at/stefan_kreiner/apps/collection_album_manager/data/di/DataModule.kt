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

package com.example.collection_album_manager.data.di

import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbumItemRepository
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbumRepository
import at.stefan_kreiner.apps.collection_album_manager.data.ProductionCollectionAlbumItemRepository
import at.stefan_kreiner.apps.collection_album_manager.data.ProductionCollectionAlbumRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsCollectionAlbumRepository(
        implementation: ProductionCollectionAlbumRepository
    ): CollectionAlbumRepository

    @Singleton
    @Binds
    fun bindsCollectionAlbumItemRepository(
        implementation: ProductionCollectionAlbumItemRepository
    ): CollectionAlbumItemRepository

//    @Singleton
//    @Binds
//    fun bindsMyModelRepository(
//        implementation: DefaultMyModelRepository
//    ): MyModelRepository
}

//class FakeCollectionAlbumRepository @Inject constructor() : CollectionAlbumRepository {
//    override val collectionAlbums: Flow<List<String>> = flowOf(fakeCollectionAlbums)
//
//    override suspend fun add(name: String) {
//        throw NotImplementedError()
//    }
//}
//
//val fakeCollectionAlbums = listOf("One", "Two", "Three")
