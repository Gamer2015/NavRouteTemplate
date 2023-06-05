package at.stefan_kreiner.apps.collection_album_manager.ui.album_view

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbum
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbumItem
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbumItemRepository
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val collectionAlbumRepository: CollectionAlbumRepository,
    private val collectionAlbumItemRepository: CollectionAlbumItemRepository,
) : ViewModel() {
    private val itemId: Long = checkNotNull(
        savedStateHandle[AlbumViewScreenNavigationDestination.itemIdArg.name]
    )

    val uiState: StateFlow<AlbumViewUiState> =
        collectionAlbumRepository.getByIdentifierWithItems(itemId)
            .map<CollectionAlbum?, AlbumViewUiState>(
                AlbumViewUiState::Success
            ).catch { emit(AlbumViewUiState.Error(it)) }.stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), AlbumViewUiState.Loading
            )

    fun changeItems(
        album: CollectionAlbum,
        changedIndices: Set<Int>,
        callback: (Throwable?) -> Unit,
    ): Job = viewModelScope.launch {
        Log.d("AlbumViewViewModel", "changeItems: album: ${album.name}")
        Log.d("AlbumViewViewModel", "changeItems: changedIndices: $changedIndices")
        val albumItems = album.items

        // Register callback with an API
        try {
            val itemsToDelete = changedIndices.intersect(albumItems).map {
                CollectionAlbumItem(
                    collectionAlbum = album,
                    itemIndex = it,
                )
            }
            Log.d("AlbumViewViewModel", "changeItems: itemsToDelete: $itemsToDelete")
            val itemsToCreate = changedIndices.minus(albumItems).map {
                CollectionAlbumItem(
                    collectionAlbum = album,
                    itemIndex = it,
                )
            }
            Log.d("AlbumViewViewModel", "changeItems: itemsToCreate: $itemsToCreate")
            collectionAlbumItemRepository.CUD(
                toInsert = itemsToCreate,
                toUpdate = listOf(),
                toDelete = itemsToDelete,
            )
            callback(null)
        } catch (error: Throwable) {
            callback(error)
        }
    }

    fun deleteAlbum(
        album: CollectionAlbum,
        callback: (Throwable?) -> Unit,
    ): Job = viewModelScope.launch {
        try {
            collectionAlbumRepository.delete(
                album
            )
            callback(null)
        } catch (error: Throwable) {
            callback(error)
        }
    }
}

sealed interface AlbumViewUiState {
    object Loading : AlbumViewUiState
    data class Error(val throwable: Throwable) : AlbumViewUiState
    data class Success(val data: CollectionAlbum?) : AlbumViewUiState
}
