package at.stefan_kreiner.apps.collection_album_manager.ui.album_insert

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import at.stefan_kreiner.apps.collection_album_manager.R
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbum
import at.stefan_kreiner.apps.collection_album_manager.data.CollectionAlbumIdentifierType
import at.stefan_kreiner.apps.collection_album_manager.data.PreCollectionAlbum
import at.stefan_kreiner.apps.collection_album_manager.ui.album_list.AlbumListScreenViewModel
import at.stefan_kreiner.apps.collection_album_manager.ui.album_list.AlbumListUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumInsertScreen(
    navigateUp: (() -> Unit)?,
    navigateToAlbum: (CollectionAlbumIdentifierType) -> Unit,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    viewModel: AlbumInsertScreenViewModel = hiltViewModel(),
    listScreenViewModel: AlbumListScreenViewModel = hiltViewModel(),
) {
    val listScreenUiState by produceState<AlbumListUiState>(
        initialValue = AlbumListUiState.Loading, key1 = lifecycle, key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            listScreenViewModel.uiState.collect { value = it }
        }
    }

    when (listScreenUiState) {
        is AlbumListUiState.Loading -> {}
        is AlbumListUiState.Error -> {}
        is AlbumListUiState.Success -> {
            val data = (listScreenUiState as AlbumListUiState.Success).data
            AlbumInsertSuccessScreen(
                collections = data,
                navigateUp = navigateUp,
                navigateToAlbum = navigateToAlbum,
                viewModel = viewModel,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumInsertSuccessScreen(
    collections: List<CollectionAlbum>,
    navigateUp: (() -> Unit)?,
    navigateToAlbum: (CollectionAlbumIdentifierType) -> Unit,
    viewModel: AlbumInsertScreenViewModel = hiltViewModel(),
) {
    val defaultCollectionName = stringResource(R.string.collection_name_input_default)

    var initialCollectionName = defaultCollectionName
    var initialAlbumNameCounter = 0
    while(collections.map {it.name}.contains(initialCollectionName)) {
        initialAlbumNameCounter += 1
        initialCollectionName = "$defaultCollectionName $initialAlbumNameCounter"
    }

    var nameInput by rememberSaveable() {
        mutableStateOf(initialCollectionName)
    }
    var itemCountInput by rememberSaveable() {
        mutableStateOf("50")
    }

    val itemCount = itemCountInput.toUIntOrNull()

    val isValidName = collections.all {
        it.name != nameInput
    }

    val isValidItemCount = itemCount != null && itemCount > 0u

    var isSaveInProgress = remember { false }
    val isSaveAllowed = isValidName && isValidItemCount && !isSaveInProgress


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val nameInputFocusRequester = remember { FocusRequester() }
    val itemCountInputFocusRequester = remember { FocusRequester() }

    val albumInsertErrorMessage = stringResource(R.string.album_insert_error_message)
    fun saveRaw() {
        Log.d("Insert", "isValidName: $isValidName")
        Log.d("Insert", "isValidItemCount: $isValidItemCount")
        Log.d("Insert", "isSaveInProgress: $isSaveInProgress")
        Log.d("Insert", "Is save allowed: $isSaveAllowed")
        if (!isSaveAllowed) return@saveRaw
        itemCount!!
        isSaveInProgress = true
        viewModel.insertCollectionAlbum(
            PreCollectionAlbum(
                name = nameInput,
                itemCount = itemCount,
                description = "",
            )
        ) { collectionAlbumIdentifier, error ->
            if (error != null) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        error.message ?: albumInsertErrorMessage
                    )
                    Log.e("Insert", "An error occured", error)
                }
            } else if (collectionAlbumIdentifier != null) {
                navigateToAlbum(collectionAlbumIdentifier)
            }
            isSaveInProgress = false
        }
    }
    fun save() {
        if (isSaveAllowed) {
            saveRaw()
            focusManager.clearFocus()
        } else if(!isValidName) {
            nameInputFocusRequester.requestFocus()
        } else if(!isValidItemCount) {
            itemCountInputFocusRequester.requestFocus()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        TopAppBar(title = {
            Text(text = stringResource(R.string.album_insert_title), color = MaterialTheme.colorScheme.onPrimary)
        }, colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ), navigationIcon = {
            if(navigateUp != null) {
                IconButton(
                    onClick = navigateUp, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back_to_collection_list_button_description),
                    )
                }
            }
        }, actions = {
            AnimatedVisibility(visible = isSaveInProgress) {
                CircularProgressIndicator(
                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
                )
            }
        })
    }, floatingActionButton = {
        if (!isSaveInProgress) {
            FloatingActionButton(
                onClick = {
                    save()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.save_album_item_changes_button_description),
                )
            }
        }
    }) {
        AlbumInsertForm(
            nameInput = nameInput,
            onNameInputChanged = {
                nameInput = it
            },
            itemCountInput = itemCountInput,
            onItemCountInputChanged = {
                itemCountInput = it
            },
            isValidName = isValidName,
            isValidItemCount = isValidItemCount,
            isSaveInProgress = isSaveInProgress,
            focusManager = focusManager,
            nameInputFocusRequester = nameInputFocusRequester,
            itemCountInputFocusRequester = itemCountInputFocusRequester,
            modifier = Modifier.padding(it),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumInsertForm(
    nameInput: String,
    isValidName: Boolean,
    itemCountInput: String,
    isValidItemCount: Boolean,
    isSaveInProgress: Boolean,
    onNameInputChanged: (String) -> Unit,
    onItemCountInputChanged: (String) -> Unit,
    focusManager: FocusManager,
    itemCountInputFocusRequester: FocusRequester,
    nameInputFocusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = nameInput,
            singleLine = true,
            modifier = Modifier.fillMaxWidth().focusRequester(nameInputFocusRequester),
            onValueChange = onNameInputChanged,
            enabled = !isSaveInProgress,
            label = {
                Text(text = stringResource(R.string.collection_album_name_textfield_label))
            },
            isError = !isValidName,
            supportingText = if (isValidName) null else ({
                Text(stringResource(R.string.duplicate_collection_name_error_text))
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = itemCountInput,
            singleLine = true,
            modifier = Modifier.fillMaxWidth().focusRequester(itemCountInputFocusRequester),
            onValueChange = onItemCountInputChanged,
            isError = !isValidItemCount,
            enabled = !isSaveInProgress,
            label = {
                Text(stringResource(R.string.total_collection_size_textfield_label))
            },
            supportingText = if (isValidItemCount) null else ({
                Text(stringResource(R.string.invalid_item_count_error_text))
            }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
        )
    }
}