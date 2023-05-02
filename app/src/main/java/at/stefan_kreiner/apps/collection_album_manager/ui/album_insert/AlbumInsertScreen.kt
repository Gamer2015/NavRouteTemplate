package at.stefan_kreiner.apps.collection_album_manager.ui.album_insert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import at.stefan_kreiner.apps.collection_album_manager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumInsertScreen(
    navigateUp: () -> Unit,
) {
    var nameInput by rememberSaveable() {
        mutableStateOf("My Collection")
    }
    var itemCountInput by rememberSaveable() {
        mutableStateOf("50")
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "New Album", color = MaterialTheme.colorScheme.onPrimary)
        }, colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ), navigationIcon = {
            IconButton(
                onClick = navigateUp, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                )
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
            )
        }
    }) {
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
                value = itemCountInput,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    itemCountInput = it
                },
                isError = itemCountInput.toUIntOrNull() == null,
                label = {
                    Text(stringResource(R.string.total_collection_size_textfield_label))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
            )
        }
    }
}