package at.stefan_kreiner.apps.collection_album_manager.ui.album_insert

import androidx.navigation.NavGraphBuilder
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute0
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.composable

object AlbumInsertScreenNavigationDestination : NavigationDestination() {
    override val route = NavigationRoute0(
        path = UniversalResourceIdentifierPath("albums", "new")
    )
}


fun AlbumInsertScreenNavigationDestination.composable(
    builder: NavGraphBuilder,
    navigateUp: () -> Unit,
    navigateToAlbumById: (Long) -> Unit,
) {
    builder.composable(
        destination = this,
    ) {
        AlbumInsertScreen(
            navigateUp = navigateUp,
            navigateToAlbum = navigateToAlbumById
        )
    }
}