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

package at.stefan_kreiner.apps.collection_album_manager.ui.album_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbum
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class AlbumListScreenViewModel @Inject constructor(
    collectionAlbumRepository: CollectionAlbumRepository,
) : ViewModel() {
    val uiState: StateFlow<AlbumListUiState> =
        collectionAlbumRepository.collectionAlbumList.map<List<CollectionAlbum>, AlbumListUiState>(
            AlbumListUiState::Success
        ).catch { emit(AlbumListUiState.Error(it)) }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AlbumListUiState.Loading
        )
}

sealed interface AlbumListUiState {
    object Loading : AlbumListUiState
    data class Error(val throwable: Throwable) : AlbumListUiState
    data class Success(val data: List<CollectionAlbum>) : AlbumListUiState
}