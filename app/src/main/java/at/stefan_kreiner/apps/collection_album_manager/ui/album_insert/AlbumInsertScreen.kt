package at.stefan_kreiner.apps.collection_album_manager.ui.album_insert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import at.stefan_kreiner.apps.collection_album_manager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumInsertScreen(
    navigateUp: () -> Unit
) {
    var nameInput by rememberSaveable() {
        mutableStateOf("My Collection")
    }
    var itemCountInput by rememberSaveable() {
        mutableStateOf("50")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "New Album")
            }, colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ), navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            })
        },
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            OutlinedTextField(
                value = nameInput,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    nameInput = it
                },
                label = {
                    Text(text = stringResource(R.string.collection_album_name_textfield_label))
                },
            )
            OutlinedTextField(
                value = itemCountInput.toString(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    itemCountInput = it
                },
                isError = itemCountInput.toUIntOrNull() == null,
                label = {
                    Text(stringResource(R.string.total_collection_size_textfield_label))
                })
        }
    }
}