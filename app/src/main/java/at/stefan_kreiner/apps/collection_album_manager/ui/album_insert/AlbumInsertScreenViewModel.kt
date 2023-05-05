package at.stefan_kreiner.apps.collection_album_manager.ui.album_insert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbumRepository
import at.stefan_kreiner.apps.collection_album_manager.data.PreCollectionAlbum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumInsertScreenViewModel @Inject constructor(
    private val collectionAlbumRepository: CollectionAlbumRepository,
) : ViewModel() {
    fun insertCollectionAlbum(
        preCollectionAlbum: PreCollectionAlbum,
        callback: (Long?, Throwable?) -> Unit,
    ) : Job = viewModelScope.launch {
        // Register callback with an API
        try {
            val identifier = collectionAlbumRepository.insert(preCollectionAlbum)
            callback(identifier, null)
        } catch (error: Throwable) {
            callback(null, error)
        }
    }
}
