package at.stefan_kreiner.apps.collection_album_manager.ui.album_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import at.stefan_kreiner.apps.collection_album_manager.R
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbum
import at.stefan_kreiner.apps.collection_album_manager.ui.composables.height

@Composable
fun AlbumListScreen(
    navigateToAlbumInsert: () -> Unit,
    navigateToAlbumView: (Long) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: AlbumListScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is AlbumListUiState.Loading -> AlbumListLoadingScreen(
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
        is AlbumListUiState.Error -> AlbumListErrorScreen(
            throwable = (uiState as AlbumListUiState.Error).throwable,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
        is AlbumListUiState.Success -> {
            val data = (uiState as AlbumListUiState.Success).data
            AlbumListSuccessScreen(
                collections = data,
                navigateToAlbumInsert = navigateToAlbumInsert,
                navigateToAlbumView = navigateToAlbumView,
                windowSizeClass = windowSizeClass,
                modifier = modifier,
            )
        }
    }
}

@Composable
fun AlbumListLoadingScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {

}

@Composable
fun AlbumListErrorScreen(
    throwable: Throwable,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumListSuccessScreen(
    collections: List<CollectionAlbum>,
    navigateToAlbumInsert: () -> Unit,
    navigateToAlbumView: (Long) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.albums_list_screen_title),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }, colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateToAlbumInsert()
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(
                paddingValues
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(collections.size) {
                val album = collections[it]
                NavigationDrawerItem(modifier = Modifier.padding(
                    top = 8.dp, start = 4.dp, end = 4.dp
                ), colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ), label = {
                    Text(
                        text = album.name,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                    )
                }, badge = {
                    Text(
                        text = "${album.items.size} / ${album.itemCount}",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }, selected = false, onClick = { navigateToAlbumView(album.identifier) })
            }
            item {
                Spacer(modifier = Modifier.height(FloatingActionButtonDefaults.height))
            }
        }
    }
}