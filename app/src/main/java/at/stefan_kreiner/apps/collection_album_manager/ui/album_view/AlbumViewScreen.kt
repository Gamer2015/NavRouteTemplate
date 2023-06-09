package at.stefan_kreiner.apps.collection_album_manager.ui.album_view

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import at.stefan_kreiner.apps.collection_album_manager.R
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbum
import at.stefan_kreiner.apps.collection_album_manager.ui.composable.SearchView
import at.stefan_kreiner.apps.collection_album_manager.ui.composable.height
import at.stefan_kreiner.apps.collection_album_manager.ui.composable.isScrollingUp
import at.stefan_kreiner.apps.collection_album_manager.ui.composable.showInterstitial
import kotlinx.coroutines.launch

private const val TAG = "AlbumViewScreen"

@Composable
fun AlbumViewScreen(
    navigateUp: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: AlbumViewScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AlbumViewScreenByUiState(
        uiState = uiState,
        navigateUp = navigateUp,
        windowSizeClass = windowSizeClass,
        modifier = modifier,
        viewModel = viewModel,
    )
}

@Composable
fun AlbumViewScreenByUiState(
    uiState: AlbumViewUiState,
    navigateUp: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: AlbumViewScreenViewModel = hiltViewModel(),
) {
    when (uiState) {
        is AlbumViewUiState.Loading -> AlbumViewLoadingScreen(
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )

        is AlbumViewUiState.Error -> AlbumViewErrorScreen(
            throwable = uiState.throwable,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )

        is AlbumViewUiState.Success -> {
            val data = uiState.data
            if (data == null) {
                AlbumViewSuccessScreenWithoutAlbum(
                    navigateUp = navigateUp,
                    windowSizeClass = windowSizeClass,
                    modifier = modifier,
                )
            } else {
                AlbumViewSuccessScreenWithAlbum(
                    album = data, navigateUp = navigateUp,
                    windowSizeClass = windowSizeClass,
                    modifier = modifier,
                    viewModel = viewModel,
                )
            }
        }
    }
}

@Composable
fun AlbumViewLoadingScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {

}

@Composable
fun AlbumViewErrorScreen(
    throwable: Throwable,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
}

@Composable
fun AlbumViewSuccessScreenWithoutAlbum(
    navigateUp: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    // AlbumInsertScreen(navigateUp = navigateUp, navigateToAlbum = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumViewSuccessScreenWithAlbum(
    album: CollectionAlbum,
    navigateUp: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: AlbumViewScreenViewModel = hiltViewModel(),
) {
    var isEditState by rememberSaveable(album.identifier) {
        mutableStateOf(false)
    }
    var changedItems by rememberSaveable(isEditState) {
        mutableStateOf(setOf<Int>())
    }

    fun toggle(index: Int) {
        if (changedItems.contains(index)) {
            changedItems = changedItems.minus(index)
        } else {
            changedItems = changedItems.plus(index)
        }
    }

    var isSaveInProgress = remember { false }
    val isEditEnabled = !isSaveInProgress && isEditState

    val context = LocalContext.current
    var isMoreActionsExpanded by remember { mutableStateOf(false) }

    var savedChanges by rememberSaveable {
        mutableStateOf(setOf<Int>())
    }
    val changesHaveBeenMade = savedChanges.isNotEmpty()

    val searchInput =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }

    fun save() {
        if (!isEditEnabled) return
        isSaveInProgress = true
        Log.d(TAG, "Changed items: $changedItems")
        viewModel.changeItems(
            album,
            changedItems,
        ) {
            if (it == null) {
                savedChanges = (savedChanges - changedItems) + (changedItems - savedChanges)
                Log.d(TAG, "Changed items: $changedItems")
            }
            isEditState = false
            isSaveInProgress = false
        }
    }

    BackHandler(enabled = isEditEnabled) {
        isEditState = false
    }

    val editAndNavigateUpInterstitial =
        stringResource(id = R.string.ad_mob_interstitial_id__edit_and_navigate_up)
    BackHandler(enabled = !isEditEnabled && changesHaveBeenMade) {
        navigateUp()
        if (changesHaveBeenMade) {
            Log.d(TAG, "Show ad")
            showInterstitial(context = context, editAndNavigateUpInterstitial)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()
    val scrollScope = rememberCoroutineScope()
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        TopAppBar(title = {
            Text(
                text = album.name, color = MaterialTheme.colorScheme.onPrimary
            )
        }, colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ), navigationIcon = {
            if (!isEditEnabled) {
                IconButton(
                    onClick = {
                        navigateUp()
                        if (changesHaveBeenMade) {
                            Log.d(TAG, "Show ad")
                            showInterstitial(
                                context = context, adUnitId = editAndNavigateUpInterstitial
                            )
                        }
                    }, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_to_collection_list_button_description),
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        isEditState = false
                    }, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.abort_collection_item_change_button_description),
                    )
                }
            }
        }, actions = {
            AnimatedVisibility(visible = isSaveInProgress) {
                CircularProgressIndicator(
                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
                )
            }

            Box(
//                modifier = Modifier.fillMaxWidth()
//                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = {
                        isMoreActionsExpanded = !isMoreActionsExpanded
                    }, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Actions",
                    )
                }


                DropdownMenu(expanded = isMoreActionsExpanded,
                    onDismissRequest = { isMoreActionsExpanded = false }) {
                    val deletionFailedErrorMessage =
                        stringResource(R.string.deletion_failed_error_message)
                    DropdownMenuItem(text = { Text(stringResource(R.string.delete_action_label)) },
                        onClick = {
                            viewModel.deleteAlbum(album) { error ->
                                if (error == null) {
                                    navigateUp()
                                } else {
                                    snackbarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            deletionFailedErrorMessage
                                        )
                                        Log.e("Delete", "An error occured", error)
                                    }
                                }
                            }
                        })
                }
            }
        })
    }, floatingActionButton = {
        if (!isEditEnabled) {
            FloatingActionButton(onClick = {
                isEditState = true
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.start_album_edit_button_description),
                )
            }
        } else {
            FloatingActionButton(onClick = {
                save()
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.save_album_item_changes_button_description),
                )
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val scrollState = rememberLazyGridState()
            AnimatedVisibility(
                (scrollState.isScrollingUp() && scrollState.canScrollForward) || !scrollState.canScrollBackward || searchInput.value != TextFieldValue(
                    ""
                )
            ) {
                SearchView(
                    value = searchInput.value,
                    onValueChange = {
                        searchInput.value = it
                        val itemPosition = searchInput.value.text.toIntOrNull()
                        if (itemPosition != null && itemPosition >= 1) {
                            scrollScope.launch {
                                scrollState.scrollToItem(
                                    itemPosition - 1
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    contentDescription = "Search",
                )
            }
            LazyVerticalGrid(
                state = scrollState,
                columns = GridCells.Adaptive(
                    // make sure the buttons are at least as big as the floating action button
                    // https://m3.material.io/components/floating-action-button/specs
                    minSize = max(
                        FloatingActionButtonDefaults.height,
                        max(ButtonDefaults.MinHeight, ButtonDefaults.MinWidth)
                    )
                ),
                contentPadding = PaddingValues(2.dp),
            ) {
                val items = album.items
                val itemCount = album.itemCount.toInt()
                items(itemCount) { index ->
                    if (changedItems.contains(index).xor(items.contains(index))) {
                        Button(
                            enabled = isEditEnabled,
                            onClick = {
                                toggle(index)
                            },
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(2.dp),
                            contentPadding = PaddingValues(
                                horizontal = 0.dp
                            )
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                softWrap = false,
                                overflow = TextOverflow.Visible,
                            )
                        }
                    } else {
                        OutlinedButton(
                            enabled = isEditEnabled,
                            onClick = {
                                toggle(index)
                            },
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(2.dp),
                            contentPadding = PaddingValues(
                                horizontal = 0.dp
                            )
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                softWrap = false,
                                overflow = TextOverflow.Visible,
                            )
                        }
                    }
                }
                item {
                    // make space for items below the fab
                    // https://m3.material.io/components/floating-action-button/specs
                    Spacer(modifier = Modifier.height(FloatingActionButtonDefaults.height))
                }
            }
        }
    }
}