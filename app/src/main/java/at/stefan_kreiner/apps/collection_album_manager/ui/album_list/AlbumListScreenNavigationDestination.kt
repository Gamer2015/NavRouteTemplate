package at.stefan_kreiner.apps.collection_album_manager.ui.album_list

import androidx.navigation.NavGraphBuilder
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.AlbumViewScreen
import at.stefan_kreiner.apps.collection_album_manager.ui.album_view.composable
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationDestination
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationGraph
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.NavigationRoute0
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.UniversalResourceIdentifierPath
import at.stefan_kreiner.apps.collection_album_manager.ui.navigation.composable


object AlbumListScreenNavigationDestination : NavigationDestination() {
    override val route = NavigationRoute0(
        path = UniversalResourceIdentifierPath("albums")
    )
}



fun AlbumListScreenNavigationDestination.composable(
    builder: NavGraphBuilder,
    navigateToAlbumInsert: () -> Unit,
    navigateToAlbumView: (Long) -> Unit,
) {
    builder.composable(
        destination = this,
    ) {
        AlbumListScreen(
            navigateToAlbumInsert = navigateToAlbumInsert,
            navigateToAlbumView = navigateToAlbumView,
        )
    }
}