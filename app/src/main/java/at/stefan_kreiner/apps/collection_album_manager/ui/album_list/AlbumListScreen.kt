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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import at.stefan_kreiner.apps.collection_album_manager.R
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbum
import at.stefan_kreiner.apps.collection_album_manager.ui.album_insert.AlbumInsertScreen
import at.stefan_kreiner.apps.collection_album_manager.ui.composables.height

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumListScreen(
    navigateToAlbumInsert: () -> Unit,
    navigateToAlbumView: (Long) -> Unit,
    modifier: Modifier = Modifier,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    viewModel: AlbumListScreenViewModel = hiltViewModel(),
) {
    val uiState by produceState<AlbumListUiState>(
        initialValue = AlbumListUiState.Loading, key1 = lifecycle, key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is AlbumListUiState.Loading -> {}
        is AlbumListUiState.Error -> {}
        is AlbumListUiState.Success -> {
            val data = (uiState as AlbumListUiState.Success).data
            if (data.isEmpty()) {
                AlbumInsertScreen(navigateUp = null, navigateToAlbum = navigateToAlbumView)
            } else {
                AlbumListSuccessScreen(
                    collections = data,
                    navigateToAlbumInsert = navigateToAlbumInsert,
                    navigateToAlbumView = navigateToAlbumView,
                )
            }
        }
    }

//    Scaffold(
//        modifier = modifier,
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = stringResource(R.string.albums_list_screen_title),
//                        color = MaterialTheme.colorScheme.onPrimary,
//                    )
//                }, colors = TopAppBarDefaults.smallTopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                )
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                navigateToAlbumInsert()
//            }) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = null,
//                )
//            }
//        },
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier.padding(
//                paddingValues
//            ),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            if (uiState is AlbumListUiState.Success) {
//                val successUiState = uiState as AlbumListUiState.Success
//                items(successUiState.data.size) {
//                    val album = successUiState.data[it]
//                    NavigationDrawerItem(modifier = Modifier.padding(
//                        top = 8.dp, start = 4.dp, end = 4.dp
//                    ), colors = NavigationDrawerItemDefaults.colors(
//                        unselectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
//                    ), label = {
//                        Text(
//                            text = album.name,
//                            color = MaterialTheme.colorScheme.onSecondaryContainer,
//                            softWrap = false,
//                            overflow = TextOverflow.Ellipsis,
//                        )
//                    }, badge = {
//                        Text(
//                            text = "${album.items.size} / ${album.itemCount}",
//                            color = MaterialTheme.colorScheme.onSecondaryContainer,
//                        )
//                    }, selected = false, onClick = { navigateToAlbumView(album.identifier) })
//                }
//                item {
//                    Spacer(modifier = Modifier.height(FloatingActionButtonDefaults.height))
//                }
//            }
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumListSuccessScreen(
    collections: List<CollectionAlbum>,
    navigateToAlbumInsert: () -> Unit,
    navigateToAlbumView: (Long) -> Unit,
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